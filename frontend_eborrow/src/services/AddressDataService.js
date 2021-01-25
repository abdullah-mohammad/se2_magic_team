import { http } from "../http-common";
import axios from "axios";

const API_KEY = "5b3ce3597851110001cf6248d76aadbfbb7243bba7248b6c3fc5ade3"

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

    async getDistanceFromLocation(source, destination) {
        let result = Infinity;
        await axios.post(
            "https://api.openrouteservice.org/v2/matrix/driving-car",
            // data
            {
                "locations":[source, destination],"metrics":["distance"],"sources":[0],"destinations":[1],"units":"km"
            },
            {
                headers: {
                    'Authorization': API_KEY,
                    'Content-Type': 'application/json; charset=utf-8',
                    'Accept': 'application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8'
                }
            }
        )
        .then(res => result = res.data.distances[0][0])
        .catch(e => console.log("Error getDistanceFromLocations-API-Call", e))
        return result
    }

}

export default new AddressDataService();