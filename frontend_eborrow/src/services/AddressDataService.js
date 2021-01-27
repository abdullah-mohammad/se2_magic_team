import { http } from "../http-common";
import axios from "axios";

const API_KEY = "5b3ce3597851110001cf6248d62474b2ff7a4204900fb5db3ff42094"

class AddressDataService {

    save(data) {
        return http.post("/address", data);
    }

    getAll() {
        return http.get("/addresses");
    }

    async getAddrGeoCode(addr) {
        let result = null;
        await axios.get("https://api.openrouteservice.org/geocode/search?api_key="+API_KEY+"&text="+addr+"&layers=address")
            .then(res => result = res.data.features[0].geometry.coordinates)
            .catch(e => console.log("Error AddressGeoCode-API-Call", e));
        return result;
    }

    async getDistanceFromLocation(source, destinations) {
        let result = Infinity;
        let locations = []
        let src = null
        destinations.forEach((destination,i) => {
            locations.push(destination)
            if(src===null && JSON.stringify(destination) === JSON.stringify(source))
                src = i
        })
        if (src === null) {
            src = locations.length
            locations.push(source)
        }
        await axios.post(
            "https://api.openrouteservice.org/v2/matrix/driving-car",
            // data
            {
                "locations":locations,"metrics":["distance"],"sources":[src],"units":"km"
            },
            {
                headers: {
                    'Authorization': API_KEY,
                    'Content-Type': 'application/json; charset=utf-8',
                    'Accept': 'application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8'
                }
            }
        )
        .then(res => result = res.data.distances)
        .catch(e => console.log("Error getDistanceFromLocations-API-Call", e))
        return result
    }

}

export default new AddressDataService();