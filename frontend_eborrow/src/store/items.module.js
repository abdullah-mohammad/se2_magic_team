import AddressDataService from "../services/AddressDataService";
import ItemDataService from "../services/ItemDataService";
import UserDataService from "../services/UserDataService";
import {getUserCurrentCoordinates} from '../support/utils'

export const items = {
    namespaced: true,
    state: {
        items: [],
        loaded: false,
        filter: false,
        filteredItems: []
    },
    mutations: {
        SET_ITEMS(state, payload) {
            state.items = payload;
        },
        SET_LOADED(state, value) {
            state.loaded = value;
        },
        SET_MY_ITEMS(state, payload) {
            state.items = payload;
        },
        GET_ITEM(state, payload) {
            state.items = payload;
        }
    },
    actions: {
        async setItems(context, filterData=null){
            try {
                context.commit('SET_LOADED', false);
                // getItems
                const res = (filterData) ? await ItemDataService.filter(filterData) : await ItemDataService.getAll();
                if(!res.data) {
                    context.commit('SET_ITEMS', []);
                    return
                }
                // setItemsInlineAdresses & geoCodes & distances from currentLocation
                let itemsWithGeoCodes=res.data; // copy of the raw items
                let checker = itemsWithGeoCodes.length-1
                // init another source if search/filter location is set
                let filterLocationInGeocode = null
                if(filterData && filterData.get('where'))
                    {
                        await AddressDataService.getAddrGeoCode(filterData.get('where')).then(geoCodeRes=>filterLocationInGeocode=geoCodeRes)
                    }
                res.data.map(async (item,i) => {
                    const userId = !isNaN(item.user) ? item.user : item.user.id
                    await UserDataService.getUserInlineAddress(userId) // set inlineAddress
                        .then(async addrRes => {
                            let geocode = null;
                            // set geoCodes 
                            await AddressDataService.getAddrGeoCode(addrRes.data).then(geoCodeRes=>geocode=geoCodeRes)
                            // getDistances
                            if(filterData && filterData.get('where')) {
                                const source = filterLocationInGeocode
                                const destination = geocode
                                await AddressDataService.getDistanceFromLocation(source, destination)
                                    .then(distRes => {
                                        // spread items Infos with AddrGeoCodes...
                                        itemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geocode, distance: distRes}
                                    })
                            } else {
                                await getUserCurrentCoordinates().then(async (position) => {
                                    const source = [position.coords.longitude, position.coords.latitude]
                                    const destination = geocode
                                    await AddressDataService.getDistanceFromLocation(source, destination)
                                        .then(distRes => {
                                            // spread items Infos with AddrGeoCodes...
                                            itemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geocode, distance: distRes}
                                        })
                                })
                            }
                            //console.log("object,", userId+" : "+addrRes.data+" : "+geocode)
                        })
                        .catch(e => {
                            console.log("Error setItemsInlineAddresses & geoCodes", e)
                        })
                    
                    if(checker--==0) {
                        context.commit('SET_LOADED', true);
                        console.log("DER", itemsWithGeoCodes)
                        /* if(itemsWithGeoCodes[0].distance)
                            itemsWithGeoCodes.sort((a,b) => (a.distance > b.distance) ? 1 : ((b.distance > a.distance) ? -1 : 0)); */
                    }
                })
                //console.log("PRIM Ende", itemsWithGeoCodes)
                context.commit('SET_ITEMS', itemsWithGeoCodes);
                return Promise.resolve(itemsWithGeoCodes);
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
        }

    },
};
