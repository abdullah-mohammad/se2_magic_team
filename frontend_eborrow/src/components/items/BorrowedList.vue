<template>
  <div>
    <div v-if="borrowedlist.length >0" class="list row">
      <div class="container">
        <h2 class="my-4 gs-title">{{ borrowedlist.length }} borrowed Items:</h2>
        <div v-for="(borrowedItem) in borrowedlist" :key="borrowedItem.id">
          <div class="row gs-tool-card">
            <div class="col-md-5">
              <img :src="getItemPicture(borrowedItem.item.picture)" class="img-fluid rounded mb-3 mb-md-0 gs-fit-image"
                   alt="">
            </div>
            <div class="col-md-7 gs-tool-card-infos img-fluid">
              <h3>{{ borrowedItem.item.title }}</h3>
              <VClamp class="p"
                      :max-height="255">
                {{ borrowedItem.item.description }}
              </VClamp>
              <div class="m-bot15"><strong>Borrowd From  : </strong>
                <span>{{
                    new Date(borrowedItem.borrowFrom).toISOString().substring(0, 10)
                  }} <strong> To :</strong> {{ new Date(borrowedItem.borrowTo).toISOString().substring(0, 10) }}</span>
              </div>
              <div class="gs-tool-card-actions ">
                <router-link :to="{ path: '/items/'+ borrowedItem.item.id}"
                             class="gs-btn-blue .gs-a btn btn-sm btn-primary pt-1 pb-1 pl-3 pr-3 mt-3"
                             style="font-family: 'GoShareFont'; border-radius: 5px; font-weight: 400; letter-spacing:1.25px; border:none;"
                          >see details
                </router-link>
              </div>
            </div>

          </div>
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
    <div v-else>
      <p :style="{color:'red'}">Sorry, there is no data 😢</p>
    </div>
  </div>
</template>

<script>
import {mapActions, mapGetters, mapState} from 'vuex';
import VClamp from "vue-clamp";
import Paginate from 'vuejs-paginate';


const MAX_NUMBER_ITEMS_PER_LIST = 5;
const API_IMG_RESOURCE = process.env.VUE_APP_API_URL + "items/get-img/";
export default {
  name: "borrowedlist",
  components:{
    Paginate,
    VClamp
  },
  computed: {
    ...mapGetters('user', ['getCurrentUser']),
    currentUser() {
      return this.$store.state.auth.user;
    },
    ...mapState('borrowedlist', ['borrowedlist']),
    getPageCount() { // total pages
      return this.borrowedlist.length / MAX_NUMBER_ITEMS_PER_LIST;
    }
  },
  methods: {
    ...mapActions({
      setBorrowedList: "borrowedlist/setBorrowedList",
    }),
    getItemPicture(img) {
      if (img != null && img !== "" && img !== undefined) {
        return API_IMG_RESOURCE + img;
      } else {
        return "http://placehold.it/700x300";
      }
    },
    paginateCallback: function (pageNum) {
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST * (pageNum - 1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    }
  }
  ,
  mounted() {
    console.log("sxv")
    this.setBorrowedList(this.currentUser.id).then(() => {
      if (this.borrowedlist.length > 0)
        this.paginateCallback(1);
    })
    console.log("s")

  }
}
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>
