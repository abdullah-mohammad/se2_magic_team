<template>
    <div class="container">
        <div class="row">
            <div class="form-group col-md-3">
                <input type="text" name="dep" class="textboxstyle" id="dep" placeholder="I want to borrow" v-model="I_want_to_borrow">
            </div>
            <div class="form-group col-md-2">
                <i class="fa fa-calendar" aria-hidden="true"></i>
                <input type="text" name="dep" class="textboxstyle" id="arival" placeholder="From" v-model="from">
            </div>
            <div class="form-group col-md-2">
                <i class="fa fa-calendar" aria-hidden="true"></i>
                <input type="text" name="dep" class="textboxstyle" id="from-date1" placeholder="Till" v-model="till">
            </div>
            <div class="form-group col-md-2">
                <i class="fa fa-map-marker" aria-hidden="true"></i>
                <input type="text" name="dep" class="textboxstyle" id="to-date" placeholder="Where" v-model="where">
            </div>
            <div class="form-group col-md-2">
                <input type="Submit" name="submit" value="Search" class="btn btn-info cst-btn" id="srch" @click="handleSearchItems()">
            </div>
        </div> 
    </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
    name: "filter-items",
    data() {
        return {
            I_want_to_borrow: "",
            from: "",
            till: "",
            where: ""
        }
    },
    methods: {
        ...mapActions({
            filterItems: "items/filterItems"
        }),
        handleSearchItems() {
            if(!this.I_want_to_borrow.trim() && !this.from.trim() && !this.till.trim() && !this.where.trim())
                alert("You must enter some filter values!")
            else {
                this.filterItems({I_want_to_borrow: this.I_want_to_borrow, from: this.from, till: this.till, where: this.where})
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
        left: 10px;
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
        padding: 8px 10px 8px 35px;
        border: none;
        border-radius: 3px;
        background: #f5f5f5;
        font-size: 14px;
        line-height: 20px;
    }
</style>