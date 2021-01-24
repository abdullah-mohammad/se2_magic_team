<template>
  <div>
    <div v-if="myitems.length > 0" class="list row">

      <!-- Page Content -->
      <div class="container">

        <!-- Page Heading -->
        <h2 class="my-4 gs-title">List of My tools: </h2>

        <div v-for="(item) in myitems.slice(startLimit, endLimit)" :key="item.id">
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
                <router-link :to="{ path: '/items/'+ item.id}"
                             class="btn btn-sm btn-primary pt-1 pb-1 pl-3 pr-3"
                            style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none; background-color: #0069d9!important;"
                             >Details
                </router-link>
                <router-link :to="{ path: '/editItem/'+item.id}"
                              id="edit-item-btn"
                             class="gs-btn-blue .gs-a btn btn-sm btn-primary pt-1 pb-1 pl-3 pr-3"
                            style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none;"
                             >Edit
                </router-link> &nbsp;
                <button class="gs-btn-red .gs-a btn btn-sm btn-danger pt-1 pb-1 pl-3 pr-3" 
                        style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none;"
                        @click="deleteItem(item.id)">
                  Delete
                </button>
              </div>
            </div>
          </div>
          <!-- /.row -->
        </div>

        <!-- Pagination -->
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
            :prev-text="'prev'"
            :next-text="'last'"
            :click-handler="paginateCallback"
        >
        </paginate>

      </div>
      <!-- /.container -->

    </div>
    <div v-else>
      <p :style="{color:'red'}">Sorry, there is no data 😢</p>
    </div>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex';
import Paginate from 'vuejs-paginate';
import VClamp from 'vue-clamp';
import ItemDataService from "@/services/ItemDataService";


const MAX_NUMBER_ITEMS_PER_LIST = 5;
const API_IMG_RESOURCE = process.env.VUE_APP_API_URL+"items/get-img/";

export default {
  name: "myitems",
  components: {
    Paginate,
    VClamp

  },
  data() {
    return {
      startLimit: 0,
      endLimit: 0,

    };
  },
  computed: {
    ...mapGetters('user', ['getCurrentUser']),
    currentUser() {
      return this.$store.state.auth.user;
    }
    ,
    ...mapState('myitems', ['myitems']),
    getPageCount() { // total pages
      return this.myitems.length / MAX_NUMBER_ITEMS_PER_LIST;
    },

  },
  methods: {
    ...mapActions({
      setMyItems: "myitems/setMyItems",
    }),
    paginateCallback: function (pageNum) {
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST * (pageNum - 1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    },
    deleteItem(id) {
      if (confirm("Do you really want to delete?")) {
        ItemDataService.delete(id)
            .then(() => {
              this.setMyItems(this.currentUser.id);
            })
            .catch((e) => {
              console.log(e);
            });
      }
    },
    getItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      } else {
        return "http://placehold.it/700x300";
      }
    }
  },
  mounted() {
    // this is call of promise: so make sure that data has been fetched before pursuiving...
    this.setMyItems(this.currentUser.id).then(() => {
      if (this.myitems.length > 0)
        this.paginateCallback(1);
    })

  },

};
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}

#edit-item-btn:hover {
  background-color:#0078c2!important;
}
</style>
