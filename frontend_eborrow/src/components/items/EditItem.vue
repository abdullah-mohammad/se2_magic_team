<template>
  <div v-if="currentItem">
     <!-- Page Heading -->
    <h2 class="my-4 gs-title">Edit Item: </h2>
    <!-- Page Heading -->

    <div>
        <div class="col-md-7 ml-0" style="display: inline-block">
          <div class="form-group">
            <label for="title" class="font-weight-bold">New Title</label>
            <input
              type="text"
              class="form-control"
              id="title"
              v-model="currentItem.title"
              placeholder="Enter a new Title"
            />
            <div v-if="messageNewTitle"
                  class="alert"
                  :class="successful ? 'alert-success' : 'alert-danger'">
              {{ messageNewTitle }}
            </div>
          </div>

          <div class="form-group">
            <label for="description" class="font-weight-bold"
              >New Description</label
            >
            <textarea
              rows="5"
              class="form-control"
              id="description"
              v-model="currentItem.description"
              placeholder="Enter a new Description"
            />
            <div v-if="messageNewDescription"
                  class="alert"
                  :class="successful ? 'alert-success' : 'alert-danger'">
              {{ messageNewDescription }}
            </div>
          </div>
          <div class="row form-group">
            <div class="col-6">
              <div class="form-group">
                <label for="availableFrom" class="font-weight-bold"
                  >Available From</label
                >
                <datepicker
                    :disabledDates="disabledDates"
                    :bootstrap-styling="true"
                    type="date"
                    id="availableFrom"
                    placeholder="from"
                    v-model="currentItem.availableFrom">
                </datepicker>
                 <div v-if="messageAvailableFrom"
                       class="alert"
                       :class="successful ? 'alert-success' : 'alert-danger'">
                    {{ messageAvailableFrom }}
                  </div>
              </div>
            </div>
            <div class="col-6">
              <div class="form-group">
                <label for="availableTo" class="font-weight-bold"
                  >Available To</label
                >
                <datepicker
                    :disabledDates="disabledDates"
                    :bootstrap-styling="true"
                    type="date"
                    id="availableTo"
                    placeholder="from"
                    v-model="currentItem.availableTo">
                </datepicker>
                 <div v-if="messageAvailableTo"
                       class="alert"
                       :class="successful ? 'alert-success' : 'alert-danger'">
                    {{ messageAvailableTo }}
                  </div>
              </div>
            </div>
            <div class="col form-group">
              <div
                v-if="messageAvailableTo || messageAvailableFrom"
                class="alert"
                :class="successful ? 'alert-success' : 'alert-danger'"
              >
                {{ messageAvailableFrom }}{{ messageAvailableTo }}
              </div>
            </div>
          </div>
          <div
            id="mobile-item-img"
            class="d-sm-none d-md-none d-lg-none d-xl-none"
          >
            <div class="form-group">
              <input
                type="file"
                @change="onImageUpload"
                id="fileImage"
                name="fileImage"
                accept="image/png, image/jpeg"
                ref="fileImage"
              />
            </div>
            <div class="form-group">
              <div id="preview">
                <img v-if="!url" :src="getCurrentItemPicture"
                      class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt=""/>
                <img v-else :src="url" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt=""/>
              </div>
            </div>
          </div>
          <button
            @click="updateItem"
            class="btn btn-sm btn-primary pt-1 pb-1 pl-4 pr-4"
            style="
              font-family: 'GoShareFont';
              background: #539ac5;
              border-radius: 5px;
              font-weight: 400;
              letter-spacing: 1.5px;
              border: none;
            "
          >
            Save
          </button>
          <a @click="goBack()" 
            class="gs-btn-red btn btn-sm btn-danger pt-1 pb-1 pl-4 pr-4 ml-3"
            style="
              font-family: 'GoShareFont';
              border-radius: 5px;
              font-weight: 600;
              letter-spacing: 1.5px;
              border: none;
            "
          >Back</a>
        </div>

        <div
          id="desktop-item-img"
          class="col-md-4 ml-3 d-none d-sm-inline-block"
          style="display: inline-block; vertical-align: top"
        >
            <img v-if="!url" :src="getCurrentItemPicture"
                  class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt=""/>
            <img v-else :src="url" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt=""/>
          <div class="form-group mt-3">
            <input
              type="file"
              @change="onImageUpload"
              id="fileImage-dsk"
              name="fileImage"
              accept="image/png, image/jpeg"
              ref="fileImage"
            />
          </div>
        </div>
      </div>

  </div>
</template>
<script>
import ItemDataService from "../../services/ItemDataService";
import {mapGetters} from "vuex";
import Datepicker from 'vuejs-datepicker';

const API_IMG_RESOURCE = process.env.VUE_APP_API_URL + "items/get-img/";

export default {
  name: "editItem",
  components: {
    Datepicker
  },
  data() {
    return {
      disabledDates: {
        to: new Date(Date.now()),
      },
      url: null,
      message: "edited",
      currentItem: null,
      fileImage: null,
      messageNewTitle: "",
      messageNewDescription: "",
      messageAvailableFrom: "",
      messageAvailableTo: "",

    };
  },
  computed: {

    getCurrentItemPicture() {
      return `${API_IMG_RESOURCE}${this.currentItem.picture}/`
    },
    ...mapGetters('user', ['getCurrentUser']),
  },
  methods: {

    currentUser() {
      return this.$store.state.auth.user;
    },
    getItem(id) {
      ItemDataService.get(id)
          .then((response) => {
            this.currentItem = response.data;
            console.log(response.data)
          })
          .catch((e) => {
            console.log(e);
          });
    },
    getItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      } else {
        return "http://placehold.it/700x300";
      }
    },
    onImageUpload(event) {
      if (event.target.files[0] != null) {
        this.fileImage = event.target.files[0];
        this.url = URL.createObjectURL(this.fileImage);
      } else {
        this.url = null;
      }
    },

    goBack() {
      this.$router.push("/myitems")
    },
    updateItem() {

      if (!this.validEditItemData()) {
        var data = new FormData();
        data.append("id", this.currentItem.id);
        data.append("title", this.currentItem.title);
        data.append("description", this.currentItem.description);
        data.append("picture", this.currentItem.picture);
        data.append("availableFrom", this.currentItem.availableFrom);
        data.append("availableTo", this.currentItem.availableTo);
        data.append("user", String(this.currentItem.user.id));
        data.append("fileImage", this.fileImage);
        confirm("Do you really want to edit this Item?");
        ItemDataService.update(this.currentItem.id, data)
            .then((response) => {
              console.log(response.data);
              this.message = "The item was updated successfully!";

            })
            .catch((e) => {
              console.log(e);
            });
      }
    },
    navigateBack() {
      this.$router.go(-1)
    },

    validEditItemData() {

      this.messageNewTitle = "";
      this.messageNewDescription = "";
      this.messageAvailableFrom = "";
      this.messageAvailableTo = "";

      var isInvalid = false

      if (this.currentItem.title === undefined || this.currentItem.title === "") {
        this.messageNewTitle = "please fill in the titel. \n";
        isInvalid = true;
      }
      if (this.currentItem.title && (this.currentItem.title.length < 3 || this.currentItem.title.length > 40)) {
        this.messageNewTitle = "Length is out of bound. \n";
        isInvalid = true;
      }
      if (this.currentItem.description === undefined || this.currentItem.description === "") {
        this.messageNewDescription = "please fill in the description. \n";
        isInvalid = true;
      }
      if (this.currentItem.description && (this.currentItem.description.length < 3 || this.currentItem.description.length > 200)) {
        this.messageNewDescription = "Length is out of bound. \n";
        isInvalid = true;
      }

       if (!this.currentItem.availableFrom && !this.currentItem.availableTo) {
         this.messageAvailableFrom = "please choose the start date. \n";
         this.messageAvailableTo = "please choose the end date. \n";
         isInvalid = true;
       }

      if (this.currentItem.availableFrom > this.currentItem.availableTo) {
        this.messageAvailableFrom += "start date can not be earlier than end date. \n";
        isInvalid = true;
      }
      return isInvalid
    },
  },
  mounted() {
    this.getItem(this.$route.params.id);
  },
};
</script>

<style>
.edit-form {
  max-width: 300px;
  margin: auto;
}


body {
  background: #eee;
}

/*panel*/
.panel {
  border: none;
  box-shadow: none;
}

.panel-heading {
  border-color: #eff2f7;
  font-size: 16px;
  font-weight: 300;
}

.panel-title {
  color: #2A3542;
  font-size: 14px;
  font-weight: 400;
  margin-bottom: 0;
  margin-top: 0;
  font-family: 'Open Sans', sans-serif;
}

/*product list*/

.prod-cat li a {
  border-bottom: 1px dashed #d9d9d9;
}

.prod-cat li a {
  color: #3b3b3b;
}

.prod-cat li ul {
  margin-left: 30px;
}

.prod-cat li ul li a {
  border-bottom: none;
}

.prod-cat li ul li a:hover, .prod-cat li ul li a:focus, .prod-cat li ul li.active a, .prod-cat li a:hover, .prod-cat li a:focus, .prod-cat li a.active {
  background: none;
  color: #ff7261;
}

.pro-lab {
  margin-right: 20px;
  font-weight: normal;
}

.pro-sort {
  padding-right: 20px;
  float: left;
}

.pro-page-list {
  margin: 5px 0 0 0;
}

.product-list img {
  width: 100%;
  border-radius: 4px 4px 0 0;
  -webkit-border-radius: 4px 4px 0 0;
}

.product-list .pro-img-box {
  position: relative;
}

.adtocart {
  background: #fc5959;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  -webkit-border-radius: 50%;
  color: #fff;
  display: inline-block;
  text-align: center;
  border: 3px solid #fff;
  left: 45%;
  bottom: -25px;
  position: absolute;
}

.adtocart i {
  color: #fff;
  font-size: 25px;
  line-height: 42px;
}

.pro-title {
  color: #5A5A5A;
  display: inline-block;
  margin-top: 20px;
  font-size: 16px;
}

.product-list .price {
  color: #fc5959;
  font-size: 15px;
}

.pro-img-details {
  margin-left: -15px;
}

.pro-img-details img {
  width: 100%;
}

.pro-d-title {
  margin-top: 0;
}

.product_meta {
  border-top: 1px solid #eee;
  border-bottom: 1px solid #eee;
  padding: 10px 0;
  margin: 15px 0;
}

.product_meta span {
  display: block;
  margin-bottom: 10px;
}

.product_meta a, .pro-price {
  /* color:#fc5959 ; */
}

.pro-price, .amount-old {
  font-size: 18px;
  padding: 0 10px;
}

.amount-old {
  text-decoration: line-through;
}

.quantity {
  width: 120px;
}

.pro-img-list {
  margin: 10px 0 0 -15px;
  width: 100%;
  display: inline-block;
}

.pro-img-list a {
  float: left;
  margin-right: 10px;
  margin-bottom: 10px;
}

.pro-d-head {
  font-size: 18px;
  font-weight: 300;
}

#preview {
  display: flex;
  justify-content: center;
  align-items: center;
}

#preview img {
  max-width: 100%;
  max-height: 500px;
}

@media screen and (max-width: 767px) {
  #desktop-item-img {
    display: none !important;
  }

  #mobile-item-img {
    display: block !important;
  }
}
</style>
