import AddressDataService from "../services/AddressDataService";
import ItemDataService from "../services/ItemDataService";
import UserDataService from "../services/UserDataService";
import {getUserCurrentCoordinates} from '../support/utils'

export const items = {
    namespaced: true,
    state: {
        items: [],
    },
    mutations: {
        SET_ITEMS(state, payload) {
            state.items = payload;
        }
    },
    actions: {
        /* async setItems(context){
            try {
                const res = await ItemDataService.getAll(); // getItems
                console.log("ZUUUM.", res.data[2].user.firstname)
                let resultWithGeoCodes=res.data;
                res.data.map(async (elem, i) => { // set Address Geocode of items owner for consuming "DistanceBetween" API
                    const addr = elem.user.address ?
                                    elem.user.address.street + ' ' + elem.user.address.streetNr + ', '
                                        + elem.user.address.zipcode + ' ' + elem.user.address.city
                                    : null
                    // spread items Infos with AddrGeoCodes
                    let geocode = null
                    if(addr) await AddressDataService.getAddrGeoCode(addr).then(res=>geocode=res)
                    resultWithGeoCodes[i] = {...elem, geocode: geocode}
                })
                context.commit('SET_ITEMS', resultWithGeoCodes);
                //context.dispatch('setItemsDistance')
                return Promise.resolve(resultWithGeoCodes);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }
            
        }, */

        async setItems(context){
            try {
                // getItems
                const res = await ItemDataService.getAll();
                // setItemsInlineAdresses & geoCodes & distances from currentLocation
                let itemsWithGeoCodes=res.data; // copy of the raw items
                res.data.map((item,i) => {
                    const userId = !isNaN(item.user) ? item.user : item.user.id
                    UserDataService.getUserInlineAddress(userId) // set inlineAddress
                        .then(async addrRes => {
                            let geocode = null;
                            // set geoCodes 
                            await AddressDataService.getAddrGeoCode(addrRes.data).then(geoCodeRes=>geocode=geoCodeRes)
                            // getDistances
                            await getUserCurrentCoordinates().then(async (position) => {
                                const source = [position.coords.longitude, position.coords.latitude]
                                const destination = geocode
                                await AddressDataService.getDistanceFromLocation(source, destination)
                                    .then(distRes => {
                                        // spread items Infos with AddrGeoCodes...
                                        itemsWithGeoCodes[i] = {...item, inlineAddress: addrRes.data, geocode: geocode, distance: distRes}
                                    })
                            })
                            //console.log("object,", userId+" : "+addrRes.data+" : "+geocode)
                        })
                        .catch(e => {
                            console.log("Error setItemsInlineAddresses & geoCodes", e)
                        })

                })
                console.log("PRIM Ende", itemsWithGeoCodes)
                context.commit('SET_ITEMS', res.data);
                return Promise.resolve(res.data);
            } catch (e) {
                console.log(e);
                return Promise.reject(e);
            }
            
        },

        /* setItemsDistance({state, dispatch}) {
            console.log("main",dispatch('getGeoCodeLocationsAsArray'))
            console.log("MUMU:", state.items)
        },  */
    },
};
