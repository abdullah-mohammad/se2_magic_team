<template>
  <div v-if="currentItem">
    <div class="container">
      <div>
        <!-- Page Heading -->
        <nav aria-label="breadcrumb" class="main-breadcrumb">
          <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="index.html">Home</a></li>
            <li class="breadcrumb-item"><a href="javascript:void(0)">Item</a></li>
            <li class="breadcrumb-item active" aria-current="page">Edit Item</li>
          </ol>
          <div class="col-md-8">
            <div class="card mt-3">
              <div class="card-body">
                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">new Title</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    <input type="text" class="form-control" placeholder="Enter a new Title"
                           v-model="currentItem.title">
                  </div>
                </div>

                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">new Description</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    <input type="text" class="form-control" placeholder="Enter a new Desc."
                           v-model="currentItem.description">
                  </div>
                </div>

                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">Available</h6>
                  </div>
                  <div class="col-sm-9 text-secondary">
                    <input type="text" class="form-control" placeholder="" v-model="currentItem.available">
                  </div>

                </div>

                <div class="row">
                  <div class="col-sm-3">
                    <h6 class="mb-0">new Photo</h6>
                  </div>
                  <div class="form-group">
                    <label for="fileImage">Upload image</label>
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
                <button @click="updateItem" id="massege" class="btn btn-success">Save</button>
                <a @click="goBack()" class="btn btn-primary">Back</a>
              </div>
            </div>
          </div>

        </nav>
      </div>
    </div>
  </div>
</template>
<script>
import ItemDataService from "../../services/ItemDataService";
import {mapGetters} from "vuex";

const API_IMG_RESOURCE = process.env.VUE_APP_API_URL+"items/get-img/";

export default {
  name: "editItem",
  data() {
    return {
      url: null,
      message: "edited",
      currentItem: null,
      fileImage: null,
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
      var data = new FormData();
      data.append("id", this.currentItem.id);
      data.append("title", this.currentItem.title);
      data.append("description", this.currentItem.description);
      data.append("picture", this.currentItem.picture);
      data.append("available", this.currentItem.available);
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
    },
    navigateBack() {
      this.$router.go(-1)
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
</style>
