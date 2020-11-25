<template>
  <div>
    <div v-if="items.length > 0" class="list row">
      <div class="col-md-8">
        <div class="input-group mb-3">
          <input
            type="text"
            class="form-control"
            placeholder="Search by title"
          />
          <div class="input-group-append">
            <button
              class="btn btn-outline-secondary"
              type="button"
            >
              Search
            </button>
          </div>
        </div>
      </div>
      
      <!-- Page Content -->
      <div class="container">

        <!-- Page Heading -->
        <h1 class="my-4">Item
          <small>list</small>
        </h1>

        <div v-for="(item) in items.slice(startLimit, endLimit)" :key="item.id">
          <!-- Item One -->
          <div class="row">
            <div class="col-md-7">
              <a href="#">
                <img class="img-fluid rounded mb-3 mb-md-0" src="http://placehold.it/700x300" alt="">
              </a>
            </div>
            <div class="col-md-5">
              <h3>{{item.title}}</h3>
              <p>{{item.description}}</p>
              <a class="btn btn-sm btn-primary" :href="'/items/' + item.id">See details</a> &nbsp;
              <a class="btn btn-sm btn-outline-success" href="#">Borrow</a>
            </div>
          </div>
          <!-- /.row -->

          <hr>
        </div>

        <!-- Pagination -->
        <paginate
            page:1
            :page-count=getPageCount
            :container-class="'pagination justify-content-center'"
            :page-class="'page-item'"
            :prev-class="'page-item'"
            :next-class="'page-item'"
            :prev-link-class="'page-link'"
            :next-link-class="'page-link'"
            :page-link-class="'page-link'"
            :prev-text="'&laquo;'"
            :next-text="'&raquo;'"
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
import { mapActions, mapState } from 'vuex';
import Paginate from 'vuejs-paginate'

const MAX_NUMBER_ITEMS_PER_LIST = 5;

export default {
  name: "items-list",
  components: {
    Paginate
  },
  data() {
    return {
      startLimit: 0,
      endLimit: 0,
    };
  },
  computed: {
    ...mapState('items', ['items']),
    getPageCount() { // total pages
      return this.items.length / MAX_NUMBER_ITEMS_PER_LIST;
    },
  },
  methods: {
    ...mapActions({
      setItems: "items/setItems"
    }),
    paginateCallback: function (pageNum){
      console.log(pageNum)
      this.startLimit = MAX_NUMBER_ITEMS_PER_LIST*(pageNum-1);
      this.endLimit = this.startLimit + MAX_NUMBER_ITEMS_PER_LIST;
    }
  },
  mounted() {
    // this is call of promise: so make sure that data has been fetched before pursuiving...
    this.setItems().then(() => {
      if(this.items.length > 0)
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
</style>
