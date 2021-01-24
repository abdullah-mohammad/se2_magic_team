export default class Item {
    constructor(title, description, picture, availableFrom,availableTo,user) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.user = user;
    }
}
