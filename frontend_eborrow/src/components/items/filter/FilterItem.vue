<template>
    <div style="position: relative; margin: auto; width: 130%; left: calc(50% - 65%);" class="container text-center">
        <div class="row">
            <div class="form-group col-md-3">
                <input type="text" name="search-item-title" class="textboxstyle" id="search-item-title" placeholder="I want to borrow" 
                    v-model="I_want_to_borrow"
                    style="padding-left: 10px; padding-right: 10px;"
                >
            </div>
            <div class="form-group col-md-2">
                <i class="fa fa-calendar" aria-hidden="true"></i>
                <input type="date" name="search-item-from" class="textboxstyle" id="search-item-from" placeholder="From" v-model="from">
            </div>
            <div class="form-group col-md-2">
                <i class="fa fa-calendar" aria-hidden="true"></i>
                <input type="date" name="search-item-till" class="textboxstyle" id="search-item-till" placeholder="Till" v-model="till">
            </div>
            <div class="form-group col-md-3">
                <i class="fa fa-map-marker" aria-hidden="true"></i>
                <input type="text" name="search-item-where" class="textboxstyle" id="search-item-where" placeholder="Where" v-model="where">
            </div>
            <div class="form-group col-md-1">
                <input type="Submit" name="submit" value="Search" class="btn btn-info cst-btn" id="srch" @click="handleSearchItems()">
            </div>
        </div> 
        <div v-if="submitted && (!this.I_want_to_borrow.trim() && !this.from.trim() && !this.till.trim() && !this.where.trim())" class="row">
            <p class="text-danger">You must enter at least one filter value!</p>
        </div>
    </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
    name: "filter-items",
    data() {
        return {
            submitted: false,
            I_want_to_borrow: "",
            from: "",
            till: "",
            where: ""
        }
    },
    methods: {
        ...mapActions({
            setItems: "items/setItems"
        }),
        handleSearchItems() {
            this.submitted = true
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
                //this.filterItems({I_want_to_borrow: this.I_want_to_borrow, from: this.from, till: this.till, where: this.where})
                //submitted = false;
            }
        }
    },
}
</script>

<style lang="css">
    .form-group {
        margin-right: 1%;
        position: relative;
        margin-bottom: 10px;
    }
    .form-group i {
        position: absolute;
        left: 20px;
        top: 10px;
        color: #191919;
        font-size: 18px;
    }
    .form-group.less-per {
        width: 16%;
        float: left;
        margin-right: 1%;
    }
    .form-group.less-btn {
        width: 5%;
        float: left;
        margin-right: 1%;
    }
    .form-group.less-btn .cst-btn {
        width: 85px;
        height: 85px;
        border-radius: 100%;
        position: absolute;
        top: -32px;
    }
    .textboxstyle {
        width: 100%;
        float: left;
        color: #191919;
        padding: 8px 10px 8px 30px;
        border: none;
        border-radius: 3px;
        background: #f5f5f5;
        font-size: 14px;
        line-height: 20px;
    }
</style>