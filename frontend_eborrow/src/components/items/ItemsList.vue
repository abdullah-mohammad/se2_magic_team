<template>
  <div class="list row">
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

      <div v-for="(item) in items" :key="item.id">
        <!-- Project One -->
        <div class="row">
          <div class="col-md-7">
            <a href="#">
              <img class="img-fluid rounded mb-3 mb-md-0" src="http://placehold.it/700x300" alt="">
            </a>
          </div>
          <div class="col-md-5">
            <h3>{{item.title}}</h3>
            <p>{{item.description}}</p>
            <a class="btn btn-sm btn-primary" href="#">View Project</a>
          </div>
        </div>
        <!-- /.row -->

        <hr>
      </div>

      <!-- Pagination -->
      <ul class="pagination justify-content-center">
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">1</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">2</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#">3</a>
        </li>
        <li class="page-item">
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
            <span class="sr-only">Next</span>
          </a>
        </li>
      </ul>

    </div>
    <!-- /.container -->

  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: "items-list",
  data() {
    return {
      
    };
  },
  computed: {
    ...mapState('items', ['items'])
  },
  methods: {
    ...mapActions({
      setItems: "items/setItems"
    })
  },
  mounted() {
    this.setItems()
    console.log(this.items)
  }
};
</script>

<style>
.list {
  text-align: left;
  max-width: 750px;
  margin: auto;
}
</style>
