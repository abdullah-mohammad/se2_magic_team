import AddressDataService from "../services/AddressDataService";
import ItemDataService from "../services/ItemDataService";
import UserDataService from "../services/UserDataService";

const MODE = {
    DISTANCE_FROM_ME: 'distanceFromMe',
    DISTANCE_FROM_FILTER_LOCATION: 'distanceFromFilterLocation'
}

const allItemEntriesDistancesEqualInfinity = (arr, mode) => arr.every(val => {
    if (mode === MODE.DISTANCE_FROM_ME) {
        return val.distanceFromMe === Infinity
    } else if(mode === MODE.DISTANCE_FROM_FILTER_LOCATION){
        return val.distanceFromFilterLocation === Infinity
    }
    return true;
});

export const items = {
    namespaced: true,
    state: {
        items: [],
        isLoaded: false,
        toFilter: false,
        nearMeItems: [],
        locationApiError: false
    },
    mutations: {
        SET_ITEMS(state, payload) {
            state.items = payload;
        },
        SET_NEAR_ME_ITEMS(state, payload) {
            state.nearMeItems = payload;
        },
        SET_LOADED(state, value) {
            state.isLoaded = value;
        },
        SET_MY_ITEMS(state, payload) {
            state.items = payload;
        },
        GET_ITEM(state, payload) {
            state.items = payload;
        },
        SET_LOCATION_API_ERROR(state, value) {
            state.locationApiError = value;
        },
    },
    actions: {
        async setItems(context, filterData=null){
            try {
                context.commit('SET_LOADED', false);
                context.commit('SET_LOCATION_API_ERROR', false);

                const res = (filterData) ? await ItemDataService.filter(filterData) : await ItemDataService.getAll();
                const userIsAuthentified = context.rootState.auth.user != null;
                // check if filter-action or not
                if(filterData === null) { // no-filter
                    // check if user is identified or not
                    if(userIsAuthentified) {    // USER IS AUTHENTIFIED calculate, sort itemList and show distance from User @ :: populate nearMeItems
                        // getItems
                        if(!res.data) {
                            context.commit('SET_NEAR_ME_ITEMS', []);
                            context.commit('SET_LOADED', true);
                            return;
                        }

                        // api-calls & Aktualisierungen möglichst vermeiden
                        if(res.data.length == context.state.nearMeItems.length){
                            console.log("LENGTHS: ", res.data.length + " :: " + context.state.nearMeItems.length)
                            console.log("SETUP_STATE: ", context.state.nearMeItems)
                            context.commit('SET_LOADED', true);
                            return
                        }
                        
                        // setNearItemsInlineAdresses & geoCodes & distances from User@ in DB
                            // get User@ in DB and transform it in geocode for 'source'
                            const connectedUserAddress = await (await UserDataService.getUserInlineAddress(context.rootState.auth.user.id)).data;
                            console.log("CONNECTED USER @: ", connectedUserAddress)
                            let connectedUserAddressInGeocode = null
                            await AddressDataService.getAddrGeoCode(connectedUserAddress).then(resGeocode=>connectedUserAddressInGeocode=resGeocode)
                            const source = connectedUserAddressInGeocode;

                            let nearMeItemsWithGeoCodes=res.data; // copy of the raw items
                            const promises =  res.data.map(async (item,i) => {
                                const userId = !isNaN(item.user) ? item.user : item.user.id
                                await UserDataService.getUserInlineAddress(userId) // set inlineAddress for each item in List
                                    .then(async addrRes => {
                                        // set geoCode 
                                        await AddressDataService.getAddrGeoCode(addrRes.data).then(async geoCodeRes=>{
                                            // spread items Infos with AddrGeoCodes...
                                            nearMeItemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geoCodeRes}
                                        }).catch(()=>nearMeItemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: [0.0,0.0]})
                                    })
                                    .catch(e => {
                                        console.log("Error by setItemsInlineAddresses & geoCodes: ", e)
                                        context.commit('SET_NEAR_ME_ITEMS', res.data);
                                        context.commit('SET_LOADED', true);
                                        alert("Error when computing distance of Item location from your address!")
                                        return;
                                    })
                                    
                            })

                            await Promise.all(promises)
                            // compute distances from source once!
                                // at first set destinations
                            let destinations = []
                            nearMeItemsWithGeoCodes.forEach(item => {
                                destinations.push(item.geocode)
                            });
                                // then compute distance and spread it...
                            await AddressDataService.getDistanceFromLocation(source, destinations).then(distRes => {
                                nearMeItemsWithGeoCodes.forEach((item, i) => {
                                    nearMeItemsWithGeoCodes[i] = {...item, distanceFromMe: distRes[0][i]}
                                })
                            })

                            if(allItemEntriesDistancesEqualInfinity(nearMeItemsWithGeoCodes, MODE.DISTANCE_FROM_ME))
                                    //alert("An API-Error occured while trying to compute distance between your address and items location")
                                    context.commit('SET_LOCATION_API_ERROR', true);

                            context.commit('SET_NEAR_ME_ITEMS', nearMeItemsWithGeoCodes);
                            context.commit('SET_LOADED', true);
                            console.log("LOADED ITEMS: ", context.state.nearMeItems)
                            return Promise.resolve(nearMeItemsWithGeoCodes);
                            /* context.commit('SET_NEAR_ME_ITEMS', nearMeItemsWithGeoCodes);
                            console.log("DER", nearMeItemsWithGeoCodes)
                            console.log("GOO", context.state.nearMeItems)
                            setTimeout(() => {
                                if(!nearMeItemsWithGeoCodes[0].geocode)
                                    alert("An API-Error occured while trying to compute distance between your address and items location")
                                context.commit('SET_LOADED', true);
                                return Promise.resolve(nearMeItemsWithGeoCodes);
                            }, 200); */


                    } else {    // USER IS NOT-AUTHENTIFIED show default items without computing distance :: populate items
                        context.commit('SET_ITEMS', res.data);
                        context.commit('SET_LOADED', true);
                        console.log("LOADED ITEMS: ", context.state.items)
                        return Promise.resolve(res.data);
                    }

                } else{ // we must filter itemList
                    if(userIsAuthentified) { // USER AUTHENTIFIED
                        if(!res.data) {
                            context.commit('SET_NEAR_ME_ITEMS', []);
                            context.commit('SET_LOADED', true);
                            return;
                        }
                        // At first: setNearItemsInlineAdresses & geoCodes & compute distances from User@ in DB
                        // get User@ in DB and transform it in geocode for 'source'
                        const connectedUserAddress = await (await UserDataService.getUserInlineAddress(context.rootState.auth.user.id)).data;
                        console.log("CONNECTED USER @: ", connectedUserAddress)
                        let connectedUserAddressInGeocode = null
                        await AddressDataService.getAddrGeoCode(connectedUserAddress).then(resGeocode=>connectedUserAddressInGeocode=resGeocode)
                        const source = connectedUserAddressInGeocode;

                        let nearMeItemsWithGeoCodes=res.data; // copy of the raw items
                        const promises =  res.data.map(async (item,i) => {
                            const userId = !isNaN(item.user) ? item.user : item.user.id
                            await UserDataService.getUserInlineAddress(userId) // set inlineAddress for each item in List
                                .then(async addrRes => {
                                    // set geoCode 
                                    await AddressDataService.getAddrGeoCode(addrRes.data).then(async geoCodeRes=>{
                                        // spread items Infos with AddrGeoCodes...
                                        nearMeItemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geoCodeRes}
                                    }).catch(()=>nearMeItemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: [0.0,0.0]})
                                })
                                .catch(e => {
                                    console.log("Error by setItemsInlineAddresses & geoCodes: ", e)
                                    context.commit('SET_NEAR_ME_ITEMS', res.data);
                                    alert("Error when computing distance of Item location from your address!")
                                    return;
                                })
                                
                        })
                        await Promise.all(promises)
                        // compute distances from source once!
                            // at first set destinations
                        let destinations = []
                        nearMeItemsWithGeoCodes.forEach(item => {
                            destinations.push(item.geocode)
                        });
                            // then compute distance and spread it...
                        await AddressDataService.getDistanceFromLocation(source, destinations).then(distRes => {
                            nearMeItemsWithGeoCodes.forEach((item, i) => {
                                nearMeItemsWithGeoCodes[i] = {...item, distanceFromMe: distRes[0][i]}
                            })
                        })
                        /* frueher war das nicht da!! */
                        if(allItemEntriesDistancesEqualInfinity(nearMeItemsWithGeoCodes, MODE.DISTANCE_FROM_ME))
                                    //alert("An API-Error occured while trying to compute distance between your address and items location")
                                    context.commit('SET_LOCATION_API_ERROR', true);
                            context.commit('SET_NEAR_ME_ITEMS', nearMeItemsWithGeoCodes);
                        /* END frueher war das nicht da!! */

                        // Now: setItemsInlineAdresses & geoCodes & distances from filter location for 'sort'
                        // init another source (filter location) if filter-location is set
                        let filterLocationInGeocode = null;
                        let sortPromises = null;
                        if(filterData.get('where'))
                        {
                                await AddressDataService.getAddrGeoCode(filterData.get('where')).then(geoCodeRes=>filterLocationInGeocode=geoCodeRes)
                            const source = filterLocationInGeocode;
                            // compute distances from source once!
                                // at first set destinations
                            let destinations = []
                            nearMeItemsWithGeoCodes.forEach(item => {
                                destinations.push(item.geocode)
                            });
                                // then compute distance and spread it...
                            await AddressDataService.getDistanceFromLocation(source, destinations).then(distRes => {
                                nearMeItemsWithGeoCodes.forEach((item, i) => {
                                    nearMeItemsWithGeoCodes[i] = {...item, distanceFromFilterLocation: distRes[0][i]}
                                })
                            })
                        }
                            
                            if(sortPromises) await Promise.all(sortPromises)
                            if(filterData.get('where') && (!nearMeItemsWithGeoCodes[0].geocode || allItemEntriesDistancesEqualInfinity(nearMeItemsWithGeoCodes, MODE.DISTANCE_FROM_FILTER_LOCATION)))
                                    //alert("An API-Error occured while trying to compute distance between the filter location and items location")
                                    context.commit('SET_LOCATION_API_ERROR', true);
                            
                            // sort result
                            if(filterData.get('where') && !allItemEntriesDistancesEqualInfinity(nearMeItemsWithGeoCodes, MODE.DISTANCE_FROM_FILTER_LOCATION)) {
                                nearMeItemsWithGeoCodes.sort((a,b) => a.distanceFromFilterLocation - b.distanceFromFilterLocation);
                            }
                                
                            context.commit('SET_NEAR_ME_ITEMS', nearMeItemsWithGeoCodes);
                            context.commit('SET_LOADED', true);
                            console.log("LOADED ITEMS: ", context.state.items)
                            return Promise.resolve(nearMeItemsWithGeoCodes);

                    } else { // USER NO-AUTHENTIFIED
                        if(!res.data) {
                            context.commit('SET_ITEMS', []);
                            context.commit('SET_LOADED', true);
                            return;
                        }
                        // setItemsInlineAdresses & geoCodes & distances from filter location for 'sort'
                        let itemsWithGeoCodes=res.data; // copy of the raw items
                        // init another source (filter location) if filter-location is set
                        let filterLocationInGeocode = null;
                        let source = null;
                        let promises = null;
                        if(filterData.get('where'))
                        {
                                await AddressDataService.getAddrGeoCode(filterData.get('where')).then(geoCodeRes=>filterLocationInGeocode=geoCodeRes)
                            source = filterLocationInGeocode;
                            promises =  res.data.map(async (item,i) => {
                                const userId = !isNaN(item.user) ? item.user : item.user.id
                                await UserDataService.getUserInlineAddress(userId) // set inlineAddress for each item in List
                                    .then(async addrRes => {
                                        // set geoCode 
                                        await AddressDataService.getAddrGeoCode(addrRes.data).then(async geoCodeRes=>{
                                            // spread items Infos with AddrGeoCodes...
                                            itemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geoCodeRes}
                                        }).catch(()=>itemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: [0.0,0.0]})
                                    })
                                    .catch(e => {
                                        console.log("Error by setItemsInlineAddresses & geoCodes: ", e)
                                        context.commit('SET_ITEMS', res.data);
                                        context.commit('SET_LOADED', true);
                                        alert("Error when computing distance of Item location from filter location!")
                                        return;
                                    })
                                    
                            })
                        }
                            

                            if(promises) await Promise.all(promises)
                            if (filterData.get('where')) {
                                // compute distances from source once!
                                    // at first set destinations
                                let destinations = []
                                itemsWithGeoCodes.forEach(item => {
                                    destinations.push(item.geocode)
                                });
                                    // then compute distance and spread it...
                                await AddressDataService.getDistanceFromLocation(source, destinations).then(distRes => {
                                    itemsWithGeoCodes.forEach((item, i) => {
                                        itemsWithGeoCodes[i] = {...item, distanceFromFilterLocation: distRes[0][i]}
                                    })
                                })
                            }

                            if(filterData.get('where') && allItemEntriesDistancesEqualInfinity(itemsWithGeoCodes, MODE.DISTANCE_FROM_FILTER_LOCATION))
                                    //alert("An API-Error occured while trying to compute distance between the filter location and items location")
                                    context.commit('SET_LOCATION_API_ERROR', true);

                            // sort result
                            if(filterData.get('where') && !allItemEntriesDistancesEqualInfinity(itemsWithGeoCodes, MODE.DISTANCE_FROM_FILTER_LOCATION)) {
                                itemsWithGeoCodes.sort((a,b) => a.distanceFromFilterLocation - b.distanceFromFilterLocation);
                            }
                                
                            context.commit('SET_ITEMS', itemsWithGeoCodes);
                            context.commit('SET_LOADED', true);
                            console.log("LOADED ITEMS: ", context.state.items)
                            return Promise.resolve(itemsWithGeoCodes);
                    }
                }


                







                
                
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }     
        },

        async setMyItems(id,context){
            try {
                const res = await ItemDataService.getMyGegenstaende(id);
                context.commit('SET_MY_ITEMS', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }

        },

        async getItem(id,context){
            try {
                const res = await ItemDataService.get(id);
                context.commit('GET_ITEM', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }
        },

        resetLocationApiError(context) {
            context.commit('SET_LOCATION_API_ERROR', false);
        }

    },
};
