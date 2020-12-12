<template>
  <div class="aboutUs">
    <h2 class="career">Career at GoShare: </h2>
        <h3 class="header"> UX Designer f/m/d</h3>
        <p> Our Hamburg desing team needs a senior UX designer. </p>
        <a href="test.com">Job description here</a>
    <hr style="width:50%;text-align:left;margin-left:0">
    <h3 class="header"> Junior Product Owner f/m/d</h3>
    <p> Our Hamburg Platform team needs a junior PO. </p>
    <a href="test.com">Job description here</a>

    <hr style="width:50%;text-align:left;margin-left:0">
    <h3 class="header"> Senior QA f/m/d</h3>
    <p> Our Hamburg Platform team needs a senior QA. </p>
    <a href="test.com">Job description here</a>

    </div>

</template>

<script>
  export default {
    name: "Career",
    props: {
      msg: String,
    },
  };
</script>

<style scoped>
  h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #539AC5;
  }
  .career{
    color: #C55353;
  }
  .header{
    font-size: 20px;
    color: #5E5E5E;
    padding-bottom: 30px;

  }

</style>