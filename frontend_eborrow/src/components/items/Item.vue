<template>
  <!-- <div v-if="currentItem" class="edit-form">
    <h4>Item</h4>
    <form>
      <div class="form-group">
        <label for="title">Title</label>
        <input
          type="text"
          class="form-control"
          id="title"
          v-model="currentItem.title"
        />
      </div>
      <div class="form-group">
        <label for="description">Description</label>
        <input
          type="text"
          class="form-control"
          id="description"
          v-model="currentItem.description"
        />
      </div>

      <div class="form-group">
        <label><strong>Status:</strong></label>
        {{ currentItem.published ? "Published" : "Pending" }}
      </div>
    </form>

    <button
      class="badge badge-primary mr-2"
      v-if="currentItem.published"
      @click="updatePublished(false)"
    >
      UnPublish
    </button>
    <button
      v-else
      class="badge badge-primary mr-2"
      @click="updatePublished(true)"
    >
      Publish
    </button>

    <button class="badge badge-danger mr-2" @click="deleteItem">Delete</button>

    <button type="submit" class="badge badge-success" @click="updateItem">
      Update
    </button>
    <p>{{ message }}</p>





  </div>

  <div v-else>
    <br />
    <p>Please click on a Item...</p>
  </div> -->



  
  <!-- <div>
    <div class="container bootdey">
      <div class="col-md-12">
      <section class="panel">
            <div class="panel-body">
                <div :style="{border: 'solid 1px red', display:'inline-block'}" class="col-md-6">
                    <div class="pro-img-details">
                        <img src="https://via.placeholder.com/550x380/FFB6C1/000000" alt="">
                    </div>
                </div>
                <div :style="{border: 'solid 1px blue', display:'inline-block', verticalAlign: 'top'}" class="col-md-6">
                    <h4 class="pro-d-title">
                        <a href="#" class="">
                            Leopard Shirt Dress
                        </a>
                    </h4>
                    <p>
                        Praesent ac condimentum felis. Nulla at nisl orci, at dignissim dolor, The best product descriptions address your ideal buyer directly and personally. The best product descriptions address your ideal buyer directly and personally.
                    </p>
                    <div class="product_meta">
                        <span class="posted_in"> <strong>Owner:</strong> <a class="text-primary" rel="tag" href="#">Max Mustermann</a> </span>
                    </div>
                    <div class="m-bot15"> <strong>Availability : </strong> <span class="amount-old">Yes</span>  <span class="pro-price"> No</span></div>
                    <p>
                        <button class="btn btn-round btn-success" type="button"><i class="fa fa-shopping-cart"></i> Borrow</button>
                    </p>
                </div>
            </div>
        </section>
        </div>
      </div>
  </div> -->


  <div v-if="currentItem">
      <!-- Page Heading -->
      <h1 class="my-4">Item
        <small>details</small>
      </h1>

      <div class="container bootdey">
      <div class="col-md-12">
      <section class="panel">
            <div class="panel-body">
                <div :style="{display:'inline-block'}" class="col-md-6">
                    <div class="pro-img-details">
                        <img style="width: 100%;
    object-fit: cover;
    height: 380px;" onerror="this.onerror=null;this.src='http://placehold.it/700x300';" :src="getCurrentItemPicture" :alt="currentItem.picture">
                    </div>
                </div>
                <div :style="{display:'inline-block', verticalAlign: 'top'}" class="col-md-6">
                    <h4 class="pro-d-title">
                            {{currentItem.title}}
                    </h4>
                    <p>
                        {{currentItem.description}}
                    </p>
                    <div class="product_meta">
                        <span class="posted_in"> <strong>Owner:</strong> <router-link class="text-primary" rel="tag" :to="{ path: '/user/'+ currentItem.user.id}">{{currentItem.user.firstname}} {{currentItem.user.lastname}}</router-link> </span>
                    </div>
                    <div class="m-bot15"> <strong>Availability : </strong>
                      <span :class="{'text-success': currentItem.available, 'amount-old': !currentItem.available}">Yes</span>  
                      <span :class="{'amount-old text-dark': currentItem.available, 'pro-price text-danger': !currentItem.available}"> No</span>
                    </div>
                    <br>
                    <p>
                        <button @click="navigateBack" class="btn btn-round btn-primary gs-btn-rounded gs-btn-blue" type="button"><i class="fa fa-chevron-left"></i> Back</button>
                        <button class="btn btn-round btn-success gs-btn-rounded" type="button"><i class="fa fa-shopping-cart"></i> Borrow</button>
                    </p>
                </div>
            </div>
        </section>
        </div>
      </div>
  </div>


</template>

<script>
import ItemDataService from "../../services/ItemDataService";

const API_IMG_RESOURCE = "http://localhost:8080/items/get-img/";

export default {
  name: "item",
  data() {
    return {
      currentItem: null,
      message: ""
    };
  },
  computed: {
    getCurrentItemPicture() {
      return `${API_IMG_RESOURCE}${this.currentItem.picture}/`
    }
  },
  methods: {
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

    updatePublished(status) {
      var data = {
        id: this.currentItem.id,
        title: this.currentItem.title,
        description: this.currentItem.description,
        published: status,
      };

      ItemDataService.update(this.currentItem.id, data)
        .then((response) => {
          this.currentItem.published = status;
          console.log(response.data);
        })
        .catch((e) => {
          console.log(e);
        });
    },

    updateItem() {
      ItemDataService.update(this.currentItem.id, this.currentItem)
        .then((response) => {
          console.log(response.data);
          this.message = "The item was updated successfully!";
        })
        .catch((e) => {
          console.log(e);
        });
    },

    deleteItem() {
      ItemDataService.delete(this.currentItem.id)
        .then((response) => {
          console.log(response.data);
          this.$router.push({ name: "items" });
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
    this.message = "";
    this.getItem(this.$route.params.id);
  },
};
</script>

<style>
.edit-form {
  max-width: 300px;
  margin: auto;
}





body{
background:#eee;
}

/*panel*/
.panel {
    border: none;
    box-shadow: none;
}

.panel-heading {
    border-color:#eff2f7 ;
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

.prod-cat li a{
    border-bottom: 1px dashed #d9d9d9;
}

.prod-cat li a {
    color: #3b3b3b;
}

.prod-cat li ul {
    margin-left: 30px;
}

.prod-cat li ul li a{
    border-bottom:none;
}
.prod-cat li ul li a:hover,.prod-cat li ul li a:focus, .prod-cat li ul li.active a , .prod-cat li a:hover,.prod-cat li a:focus, .prod-cat li a.active{
    background: none;
    color: #ff7261;
}

.pro-lab{
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

.product-list img{
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

.adtocart i{
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
    color:#fc5959 ;
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
.product_meta a, .pro-price{
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
