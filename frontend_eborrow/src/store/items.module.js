import AddressDataService from "../services/AddressDataService";
import ItemDataService from "../services/ItemDataService";
import UserDataService from "../services/UserDataService";
import {getUserCurrentCoordinates} from '../support/utils'

export const items = {
    namespaced: true,
    state: {
        items: [],
        loaded: false
    },
    mutations: {
        SET_ITEMS(state, payload) {
            state.items = payload;
        },
        SET_LOADED(state, value) {
            state.loaded = value;
        }
    },
    actions: {
        async setItems(context){
            try {
                context.commit('SET_LOADED', false);
                // getItems
                const res = await ItemDataService.getAll();
                // setItemsInlineAdresses & geoCodes & distances from currentLocation
                let itemsWithGeoCodes=res.data; // copy of the raw items
                let checker = itemsWithGeoCodes.length-1
                res.data.map(async (item,i) => {
                    const userId = !isNaN(item.user) ? item.user : item.user.id
                    await UserDataService.getUserInlineAddress(userId) // set inlineAddress
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
                        
                    if(checker--==0) {
                        context.commit('SET_LOADED', true);
                        console.log("DER", itemsWithGeoCodes)
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
    },
};
