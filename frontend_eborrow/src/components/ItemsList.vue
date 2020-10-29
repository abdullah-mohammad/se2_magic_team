<template>
  <div class="list row">
    <div class="col-md-8">
      <div class="input-group mb-3">
        <input
          type="text"
          class="form-control"
          placeholder="Search by title"
          v-model="title"
        />
        <div class="input-group-append">
          <button
            class="btn btn-outline-secondary"
            type="button"
            @click="searchTitle"
          >
            Search
          </button>
        </div>
      </div>
    </div>
    <div class="col-md-6">
      <h4>Items List</h4>
      <ul class="list-group">
        <li
          class="list-group-item"
          :class="{ active: index == currentIndex }"
          v-for="(item, index) in items"
          :key="index"
          @click="setActiveItem(item, index)"
        >
          {{ item.title }}
        </li>
      </ul>

      <button class="m-3 btn btn-sm btn-danger" @click="removeAllItems">
        Remove All
      </button>
    </div>
    <div class="col-md-6">
      <div v-if="currentItem">
        <h4>Item</h4>
        <div>
          <label><strong>Title:</strong></label> {{ currentItem.title }}
        </div>
        <div>
          <label><strong>Description:</strong></label>
          {{ currentItem.description }}
        </div>
        <div>
          <label><strong>Status:</strong></label>
          {{ currentItem.published ? "Published" : "Pending" }}
        </div>

        <a class="badge badge-warning" :href="'/items/' + currentItem.id">
          Edit
        </a>
      </div>
      <div v-else>
        <br />
        <p>Please click on a Item...</p>
      </div>
    </div>
  </div>
</template>

<script>
import ItemDataService from "../services/ItemDataService";

export default {
  name: "items-list",
  data() {
    return {
      items: [],
      currentItem: null,
      currentIndex: -1,
      title: "",
    };
  },
  methods: {
    retrieveItems() {
      ItemDataService.getAll()
        .then((response) => {
          this.items = response.data;
        })
        .catch((e) => {
          console.log(e);
        });
    },

    refreshList() {
      this.retrieveItems();
      this.currentItem = null;
      this.currentIndex = -1;
    },

    setActiveItem(item, index) {
      this.currentItem = item;
      this.currentIndex = index;
    },

    removeAllItems() {
      ItemDataService.deleteAll()
        .then((response) => {
          console.log(response.data);
          this.refreshList();
        })
        .catch((e) => {
          console.log(e);
        });
    },

    searchTitle() {
      ItemDataService.findByTitle(this.title)
        .then((response) => {
          this.items = response.data;
          console.log(response.data);
        })
        .catch((e) => {
          console.log(e);
        });
    },
  },
  mounted() {
    this.retrieveItems();
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
