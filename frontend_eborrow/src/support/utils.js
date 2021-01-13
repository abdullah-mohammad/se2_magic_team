export function getUserCurrentCoordinates() {
    if (navigator.geolocation) {
        return new Promise((resolve, reject) => 
            navigator.geolocation.getCurrentPosition(resolve, reject)
        );
    } else { 
        alert("Geolocation is not supported by this browser!")
        //alert('You should activate Geolocation on your browser to allow filtering items from your current location!');
    }
}