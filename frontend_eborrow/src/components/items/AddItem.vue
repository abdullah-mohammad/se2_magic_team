<template>
  <div class="container">
    <div v-if="!$store.state.auth.user">
      If you
      <router-link to="/login">log in</router-link>
      , you can add your items to share them.
    </div>
    <div class="submit-form" v-else>
      <!-- Page Heading -->
      <h2 class="my-4 gs-title">Add new Item:</h2>
      <!-- Page Heading -->
      <div v-if="!submitted">
        <div class="col-md-7 ml-0" style="display: inline-block">
          <div class="form-group">
            <label for="title" class="font-weight-bold">Title</label>
            <input
              type="text"
              class="form-control"
              id="title"
              v-model="item.title"
            />

            <div
              v-if="messageTitle"
              class="alert"
              :class="successful ? 'alert-success' : 'alert-danger'"
            >
              {{ messageTitle }}
            </div>
          </div>
          <div class="form-group">
            <label for="description" class="font-weight-bold"
              >Description</label
            >
            <textarea
              rows="5"
              class="form-control"
              id="description"
              v-model="item.description"
            />

            <div
              v-if="messageDescription"
              class="alert"
              :class="successful ? 'alert-success' : 'alert-danger'"
            >
              {{ messageDescription }}
            </div>
          </div>
          <div class="row form-group">
            <div class="col-6">
              <div class="form-group">
                <label for="availableFrom" class="font-weight-bold"
                  >available from</label
                >
                <datepicker
                  :disabledDates="disabledDates"
                  :bootstrap-styling="true"
                  id="availableFrom"
                  placeholder="from"
                  v-model="item.availableFrom"
                >
                </datepicker>
              </div>
            </div>
            <div class="col-6">
              <div class="form-group">
                <label for="availableTo" class="font-weight-bold"
                  >available to</label
                >
                <datepicker
                  :disabledDates="disabledDates"
                  :bootstrap-styling="true"
                  type="date"
                  id="availableTo"
                  placeholder="from"
                  v-model="item.availableTo"
                >
                </datepicker>
              </div>
            </div>
            <div class="col form-group">
              <div
                v-if="messageAvailableTo || messageAvailableFrom"
                class="alert"
                :class="successful ? 'alert-success' : 'alert-danger'"
              >
                {{ messageAvailableFrom }}{{ messageAvailableTo }}
              </div>
            </div>
          </div>
          <div
            id="mobile-item-img"
            class="d-sm-none d-md-none d-lg-none d-xl-none"
          >
            <div class="form-group">
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
                <img
                  :src="url ? url : 'https://via.placeholder.com/150'"
                  class="img-thumbnail"
                  width="275"
                  alt="Item picture"
                />
              </div>
            </div>
          </div>
          <button
            @click="saveItem"
            class="btn btn-sm btn-primary pt-1 pb-1 pl-5 pr-5"
            style="
              font-family: 'GoShareFont';
              background: #539ac5;
              border-radius: 5px;
              font-weight: 600;
              letter-spacing: 1.5px;
              border: none;
            "
          >
            Share
          </button>
        </div>

        <div
          id="desktop-item-img"
          class="col-md-4 ml-3 d-none d-sm-inline-block"
          style="display: inline-block; vertical-align: top"
        >
          <img
            :src="url ? url : 'https://via.placeholder.com/150'"
            alt="Item picture"
            class="img-thumbnail"
            width="320"
          />
          <div class="form-group mt-3">
            <input
              type="file"
              @change="onImageUpload"
              id="fileImage-dsk"
              name="fileImage"
              accept="image/png, image/jpeg"
              ref="fileImage"
            />

          </div>
          <div class="form-group  ">
            <div
                v-if="messageImage"
                class="alert"
                :class="successful ? 'alert-success' : 'alert-danger'"
            >
              {{ messageImage }}
            </div>
          </div>
        </div>
      </div>

      <div v-else>
        <h4>You submitted successfully!</h4>
        <br />
        <button
          class="btn btn btn-sm btn-success pt-1 pb-1 pl-5 pr-5"
          @click="newItem"
        >
          Add
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import ItemDataService from "../../services/ItemDataService";
import { mapGetters } from "vuex";
import Datepicker from "vuejs-datepicker";

export default {
  name: "add-item",
  components: {
    Datepicker,
  },
  data() {
    return {
      disabledDates: {
        to: new Date(Date.now()),
      },
      item: {
        id: null,
        title: "",
        description: "",
        fileImage: null,
        published: false,
        availableFrom: null,
        availableTo: null,
      },
      submitted: false,
      url: null,
      messageTitle: "",
      messageDescription: "",
      messageAvailableFrom: "",
      messageAvailableTo: "",
      messageImage: "",
    };
  },
  computed: mapGetters("user", ["getCurrentUser"]),
  methods: {
    saveItem() {
      if (!this.validAddItemData()) {
        var data = new FormData();
        data.append("title", this.item.title);
        data.append("description", this.item.description);
        data.append(
          "availableFrom",
          new Date(this.item.availableFrom).toISOString().substring(0, 10)
        );
        data.append(
          "availableTo",
          new Date(this.item.availableTo).toISOString().substring(0, 10)
        );
        data.append("user", String(this.$store.state.auth.user.id));
        data.append("fileImage", this.item.fileImage);

        ItemDataService.create(data)
          .then((response) => {
            this.item.id = response.data.id;
            this.submitted = true;
          })
          .catch((e) => {
            console.log(e);
          });
      }
    },

    newItem() {
      this.submitted = false;
      this.item = {};
      this.url = null;
    },

    onImageUpload(event) {
      this.messageImage = "";
      if (event.target.files[0] != null) {
        this.item.fileImage = event.target.files[0];
        if(Math.round(this.item.fileImage.size/(1024*1024)) > 1){
          this.messageImage = "the image should not be bigger than 1 MB.";
          this.url = null;
        }else{
          this.url = URL.createObjectURL(this.item.fileImage);
        }
      } else {
        this.url = null;
      }
    },

    validAddItemData() {
      this.messageTitle = "";
      this.messageDescription = "";
      this.messageAvailableFrom = "";
      this.messageAvailableTo = "";

      var isInvalid = false;

      if (this.item.title === undefined || this.item.title === "") {
        this.messageTitle = "please fill in the titel. \n";
        isInvalid = true;
      }
      if (
        this.item.title &&
        (this.item.title.length < 3 || this.item.title.length > 40)
      ) {
        this.messageTitle = "Length is out of bound. \n";
        isInvalid = true;
      }
      if (this.item.description === undefined || this.item.description === "") {
        this.messageDescription = "please fill in the description. \n";
        isInvalid = true;
      }
      if (
        this.item.description &&
        (this.item.description.length < 3 || this.item.description.length > 200)
      ) {
        this.messageDescription = "Length is out of bound. \n";
        isInvalid = true;
      }

      if (!this.item.availableFrom) {
        this.messageAvailableFrom = "please choose the start date. \n";
        isInvalid = true;
      }

      if (!this.item.availableTo) {
        this.messageAvailableTo = "please choose the end date. \n";
        isInvalid = true;
      }

      if (this.item.availableFrom > this.item.availableTo) {
        this.messageAvailableFrom +=
          "start date can not be earlier than end date. \n";
        isInvalid = true;
      }
      return isInvalid;
    },
  },
};
</script>

<style>
/* .submit-form {
        max-width: 300px;
        margin: auto;
    } */

#preview {
  display: flex;
  justify-content: center;
  align-items: center;
}

#preview img {
  max-width: 100%;
  max-height: 500px;
}

@media screen and (max-width: 767px) {
  #desktop-item-img {
    display: none !important;
  }

  #mobile-item-img {
    display: block !important;
  }
}
</style>
