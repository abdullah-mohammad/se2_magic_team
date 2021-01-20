<template>
  <div class="hello">
    <br>
    <br>
    <br>
    <br>
    <div class="row">
      <div class="col-sm-6">
        <h2 style="">
          <b>
            Buy only what you need.
          </b>
        </h2>
      </div>
      <div class="col-sm-6">
        <p>
          The world without unnecessary purchases.
          Discover the sharing possibilities of your
          community.
        </p>
      </div>
    </div>

    <br>

    <div class="row">
      <div class="col-sm-3">
        <label class="col form-label">I want to borrow:</label>
        <input type="text" class="col form-control" placeholder="" value="" required="" v-model="I_want_to_borrow">
      </div>
      <div class="col-sm-2">
        <label class="col form-label">From</label>
        <input type="date" class="col form-control" placeholder="" value="" required="" v-model="from">
      </div>
      <div class="col-sm-2">
        <label class="col form-label">Till</label>
        <input type="date" class="col form-control" placeholder="" value="" required="" v-model="till">
      </div>
      <div class="col-sm-3">
        <label class="col form-label">Where:</label>
        <input type="text" class="col form-control" placeholder="" value="" required="" v-model="where">
      </div>
      <div class="col-sm-2">
        <label class="col form-label">GO</label>
        <button class="col btn btn-primary" type="submit" @click.once="showItems = !showItems" @click="itemsSuchen">
          <b>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-search"
                 viewBox="0 0 16 16">
              <path
                  d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
            </svg>
          </b>
        </button>
      </div>
    </div>
    <br>
    <div v-if="!loaded">
          <atom-spinner
            :animation-duration="1000"
            :size="60"
            :color="'#ff1d5e'"
          />
        Loading filter from current postion...
        </div>
    <div class="row" v-if="showItems" @submit="itemsSuchen">
      <div v-for="(item) in items" :key="item.id">
        <!-- Item One -->
        <div class="row gs-tool-card">
          <div class="col-md-5">
            <!-- <a href="#"> -->
            <img :src="getItemPicture(item.picture)" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image" alt="">
            <!-- http://placehold.it/700x300 -->
            <!-- </a> -->
          </div>
          <div class="col-md-7 gs-tool-card-infos img-fluid">
            <h3>{{ item.title }}</h3>
            <VClamp class="p"
                    :max-height="255"
            >
              {{ item.description }}
            </VClamp>
                        <div class="gs-tool-card-actions">
                          <span v-if="item.distance != Infinity" class="text-muted">{{item.distance}} km from you </span> &nbsp;
                          <router-link :to="{ path: '/items/'+ item.id}"
                                       class="btn btn-sm btn-rounded btn-primary gs-btn-blue .gs-a">See details
                          </router-link>
                          <router-link  v-if = "item.user.id!==currentUser.id"  :to="{ path: '/borrow/' + item.id}" class="btn btn-sm btn-outline-danger gs-btn-red .gs-a">Borrow
                          </router-link>
                        </div>
          </div>
        </div>
        <!-- /.row -->
      </div>

      <paginate
          page:1
          :page-count=getPageCount
          :container-class="'pagination justify-content-center'"
          :page-class="'page-item gs-page-item'"
          :prev-class="'page-item gs-page-item'"
          :next-class="'page-item gs-page-item'"
          :prev-link-class="'page-link gs-page-link'"
          :next-link-class="'page-link gs-page-link'"
          :page-link-class="'page-link gs-page-link'"
          :prev-text="'&laquo;'"
          :next-text="'&raquo;'"
          :click-handler="paginateCallback"
      >
      </paginate>

    </div>
  </div>
</template>

<script>
import {mapActions, mapState} from 'vuex';
import Paginate from 'vuejs-paginate';
import VClamp from 'vue-clamp';
import {AtomSpinner} from 'epic-spinners'

const MAX_NUMBER_ITEMS_PER_LIST = 5;
const API_IMG_RESOURCE = "http://localhost:8080/items/get-img/";

export default {
  name: "Home",
  props: {
    msg: String,
  },

  components: {
    Paginate,
    VClamp,
    AtomSpinner
  },
  data() {
    return {
      myItems: [],
      showItems: false,
      I_want_to_borrow: "",
      where: "",
      from: "",
      till: "",
      startLimit: 0,
      endLimit: 0,

      currentItem: null,
      submitted: false,
    };
  },
  computed: {
    ...mapState('items', ['items', 'loaded']),
    getPageCount() { // total pages
      return this.items.length / MAX_NUMBER_ITEMS_PER_LIST;
    },

    currentUser() {
      return this.$store.state.auth.user;
    },
    ...mapState('user', {
      user: state => state.user
    }),

  },
  methods: {
    ...mapActions({
      setItems: "items/setItems"
    }),
    paginateCallback: function (pageNum) {
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST * (pageNum - 1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    },
    getItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      } else {
        return "http://placehold.it/700x300";
      }
    },

    itemsSuchen() {
        var data = new FormData();
        data.append("title", this.I_want_to_borrow);
        data.append("from", this.from);
        data.append("till", this.till);
        data.append("where", this.where);
        if(this.I_want_to_borrow.trim() || this.from.trim() || this.till.trim() || this.where.trim()) {
                this.setItems(data)
                        .then((response) => {
                            console.log(response)
                        })
                        .catch((e) => {
                            console.log(e);
                        });
            }
    },

  },
  mounted() {
    // this is call of promise: so make sure that data has been fetched before pursuiving...
    this.setItems().then(() => {
      if (this.items.length > 0)
        this.paginateCallback(1);
    })
  },

};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h2 {
  color: #C55353;
  width: 18rem;
  font-size: 2.6em;
}

button {
  background-color: #C55353;
  border: #C55353;
  border-radius: 10px;
  padding: 10px 10px;
  font-size: 10px;
}

button:hover {
  background: #C55353;
}

button:focus {
  background-color: #C55353;
}

input {
  box-shadow: 0px 0px 1px 1px;
  border: none;
  border-radius: 10px;
  /*border: 2px solid lightsteelblue;*/
}
</style>