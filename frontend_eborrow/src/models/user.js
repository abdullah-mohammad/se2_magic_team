export default class User {
  constructor(username, password, firstname, lastname, email, gender, profilepicture, birthdate) {
    this.username = username;
    this.password = password;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.gender = gender;
    this.profilepicture = profilepicture;
    this.birthdate = birthdate;
  }
}
