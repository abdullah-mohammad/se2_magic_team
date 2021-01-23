<template>
  <div>
    <div v-if="!currentUser">
      If you
      <router-link to="/login">log in</router-link>
      , you can add you items to share them.
    </div>
    <div v-if="!submitted">
      <div v-if="currentItem">
        <!-- Page Heading -->
        <h2 class="my-4 gs-title">borrow item: </h2>
        <div class="container bootdey">
          <div class="col-md-12">
            <section class="panel">
              <div class="panel-body">
                <div :style="{display:'inline-block'}" class="col-md-6">
                  <div class="pro-img-details">
                    <img style="width: 100%;
    object-fit: cover;
    height: 380px;" :src="getCurrentItemPicture(currentItem.picture)"
                         :alt="currentItem.picture">
                  </div>
                </div>
                <div :style="{display:'inline-block', verticalAlign: 'top'}" class="col-md-6">
                  <h4 class="pro-d-title">
                    {{ currentItem.title }}
                  </h4>
                  <div class="product_meta">
                  <span class="posted_in"> <strong>Owner:</strong> <router-link class="text-primary" rel="tag"
                                                                                :to="{ path: '/user/'+ currentItem.user.id}">{{
                      currentItem.user.firstname
                    }} {{ currentItem.user.lastname }}</router-link> </span>
                  </div>
                  <div class="m-bot15"><strong>Availability : </strong>
                    <span>{{
                        new Date(currentItem.availableFrom).toISOString().substring(0, 10)
                      }} -> {{ new Date(currentItem.availableTo).toISOString().substring(0, 10) }}</span>
                  </div>
                  <br>
                  <div class="">
                    <div class="form-group">
                      <label for="availableFrom">borrow from</label>
                      <datepicker
                          :disabledDates="disabledDates"
                          :bootstrap-styling="true"
                          type="date"
                          id="availableFrom"
                          format="dd-MM-yyyy"
                          placeholder="from"
                          v-model="borrowFrom">
                      </datepicker>
                    </div>
                    <div class="form-group">
                      <label for="availableTo">borrow to</label>
                      <datepicker
                          :disabledDates="disabledDates"
                          :bootstrap-styling="true"
                          type="date"
                          id="availableTo"
                          format="dd-MM-yyyy"
                          placeholder="from"
                          v-model="borrowTo">
                      </datepicker>
                      <div v-if="messageBorrowDate"
                           class="alert"
                           :class="successful ? 'alert-success' : 'alert-danger'">
                        {{ messageBorrowDate }}
                      </div>
                    </div>
                  </div>
                  <br>
                  <p>
                    <button @click="navigateBack"
                            class="btn btn-round btn-primary gs-btn-rounded gs-btn-blue"
                            type="button"><i class="fa fa-chevron-left"></i> Back
                    </button>
                    <button @click="borrowItem" v-if="currentItem.user.id!==currentUser.id"

                            class="btn btn-round btn-success gs-btn-rounded" type="button"><i
                        class="fa fa-shopping-cart"></i> Borrow
                    </button>
                  </p>
                </div>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <h4>successfully</h4>
    </div>
  </div>
</template>

<script>
import ItemDataService from "../services/ItemDataService";
import BorrowDataService from "../services/BorrowDataService";
import {mapState} from 'vuex';
import Datepicker from 'vuejs-datepicker';

const API_IMG_RESOURCE = process.env.VUE_APP_API_URL + "items/get-img/";

export default {
  name: "borrow",
  components: {
    Datepicker
  },
  data() {
    return {
      count: 0,
      disabledDates: {
        ranges: [],
        dates: []
      },
      currentItem: null,
      borrowFrom: null,
      borrowTo: null,
      submitted: false,
      messageBorrowDate: "",
    };
  },
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    ...mapState('user', {
      user: state => state.user
    }),
  },
  methods: {
    getItem(id) {
      ItemDataService.get(id)
          .then((response) => {
            this.currentItem = response.data;
            this.disabledDates.ranges.push({
              from: new Date(0, 0, 0),
              to: Date.parse(new Date(this.currentItem.availableFrom).toISOString().substring(0, 10))
            });

            var current = new Date(this.currentItem.availableFrom).toISOString().substring(0, 10);
            var followingDay = new Date(current);
            followingDay.setDate(followingDay.getDate());
            followingDay.toLocaleDateString();
            this.disabledDates.dates.push(followingDay);

            this.disabledDates.ranges.push({
              from: Date.parse(new Date(this.currentItem.availableTo).toISOString().substring(0, 10)),
              to: new Date(9999, 0, 0)
            });
          })
          .catch((e) => {
            console.log(e);
          });
    },
    getBorrosbyItemId(id) {
      BorrowDataService.getByItemId(id)
          .then((res) => {
            res.data.forEach(myBorrow => this.disabledDates.dates.push(new Date(myBorrow.borrowTo)));

            res.data.forEach(myBorrow => this.disabledDates.ranges.push({
              from: new Date(myBorrow.borrowFrom),
              to: new Date(myBorrow.borrowTo)
            }))
          }).catch((e) => {
        console.log(e);
      });
    },
    navigateBack() {
      this.$router.go(-1)
    },

    borrowItem() {
      if (!this.validBorrowData()) {
        var data = {
          itemId: this.currentItem.id,
          userId: this.currentUser.id,
          borrowFrom: this.borrowFrom,
          borrowTo: this.borrowTo,
        };
        BorrowDataService.create(data).then(() => {
          this.submitted = true;
        }).catch((e) => {
          this.submitted = false;
          console.log(e);
        })
      }
    },

    getCurrentItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      } else {
        return "http://placehold.it/700x300";
      }
    },

    validBorrowData() {
      var isInvalid = false;

      this.messageBorrowDate = "";

      if (!this.borrowFrom) {
        this.messageBorrowDate = "please choose the start date. \n";
        isInvalid = true;
      }

      if (!this.borrowTo) {
        this.messageBorrowDate += "please choose the end date. \n";
        isInvalid = true;
      }

      if (this.borrowFrom > this.borrowTo) {
        this.messageBorrowDate += "start date can not be earlier than end date. \n";
        isInvalid = true;
      }

      var data = {
        itemId: this.currentItem.id,
        userId: this.currentUser.id,
        borrowFrom: this.borrowFrom,
        borrowTo: this.borrowTo,
      };
      BorrowDataService.getBorrowedCount(data).then((res) => {
        this.count = res.data;
        if (res.data !== 0) {
          this.messageBorrowDate += "conflict dates. \n";
          isInvalid = true;
          return true;
        }
      }).catch((e) => {
        console.log(e);
      });

      return isInvalid
    },
  },

  mounted() {
    this.getItem(this.$route.params.id);
    this.getBorrosbyItemId(this.$route.params.id);
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
