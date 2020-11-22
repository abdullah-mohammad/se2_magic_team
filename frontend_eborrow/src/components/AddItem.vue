<template>
  <div class="submit-form">
    <div v-if="!submitted">
        <div class="form-group">
          <label for="title">Title</label>
          <input
            type="text"
            class="form-control"
            id="title"
            required
            v-model="item.title"
            name="title"
          />
        </div>

        <div class="form-group">
          <label for="description">Description</label>
          <input
            class="form-control"
            id="description"
            required
            v-model="item.description"
            name="description"
          />
        </div>
        <!-- COMPLETED Form bild hochladen -->
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

        <button @click="saveItem" class="btn btn-success">Submit</button>
    </div>

    <div v-else>
      <h4>You submitted successfully!</h4>
      <button class="btn btn-success" @click="newItem">Add</button>
    </div>
  </div>
</template>

<script>
import ItemDataService from "../services/ItemDataService";

export default {
  name: "add-item",
  data() {
    return {
      item: {
        id: null,
        title: "",
        description: "",
        fileImage: null,
        published: false,
        user: this.$store.state.auth.user.id
      },
      submitted: false,
      
    };
  },
  methods: {
    saveItem() {
      /* var data = {
        title: this.item.title,
        description: this.item.description,
        available: false,
        user: String(this.$store.state.auth.user.id),
        fileImage: this.item.fileImage,
      }; */
      
      var data = new FormData();
      data.append("title",this.item.title);
      data.append("description",this.item.title);
      data.append("available",false);
      data.append("user",String(this.$store.state.auth.user.id));
      data.append("fileImage",this.item.fileImage); 
      

      ItemDataService.create(data)
        .then((response) => {
          this.item.id = response.data.id;
          this.submitted = true;
        })
        .catch((e) => {
          console.log(e);
        });
    },

    newItem() {
      this.submitted = false;
      this.item = {};
    },

    // COMPLETED select image
    onImageUpload(event) {
      this.item.fileImage = event.target.files[0];
      console.log(this.item.fileImage);
    },
  },
};
</script>

<style>
.submit-form {
  max-width: 300px;
  margin: auto;
}
</style>
