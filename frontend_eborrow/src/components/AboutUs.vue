<template>
  <div class="aboutUs">
    <h2 class="my-4 gs-title">About us: </h2>
    <p class="text"> We are a GoShare Team - students of Angewandte Informatik at HAW University. This semester we study Software Engineering 2
      and build our own website. For our project we chose to build a sharing platform, where locals can share tools and objects with their neighbors.
      This is an ethical alternative to buying - instead of continually purchasing unnecessary products people can rent for a fair price from local community.
    </p>
    <h3 class="team">Meet our team: </h3>

    <div class="row">

      <div class="col-5">
        <div class="image-container">
          <img src="MM.jpeg" alt="MM" width="250" height="333" class="img-about">
        </div>
        <p>Matthias Mildenberger - System Architecto</p>
      </div>

      <div class="col-5">
        <div class="image-container">
          <img src="AZ.jpeg" alt="AZ" width="250" height="333" class="img-about">
        </div>
        <p>Alaa Zetauny - Product Owner</p>
      </div>
      <div class="col-5">
        <div class="image-container">
          <img src="AD.jpeg" alt="AD" width="240" height="333" class="img-about">
        </div>
        <p>Aleksandra Dymchishina - Agile PM</p>
      </div>

      <div class="col-5">
        <div class="image-container">
          <img src="AB.jpeg" alt="AD" width="320" height="333" class="img-about">
        </div>
        <p>Abdullah Al Mohammad - Quality Assurance</p>
      </div>

      <div class="col-5">
        <div class="image-container">
          <img src="WR.jpeg" alt="AD" width="240" height="333" class="img-about">
        </div>
        <p>Ward Hiswani Zarour</p>
      </div>

      <div class="col-5">
        <div class="image-container">
          <img src="VK.jpeg" alt="AD" width="270" height="333" class="img-about">
        </div>
        <p>Venance Konan - Project Leader</p>
      </div>

      <div class="col-5">
        <div class="image-container">
          <img src="WL.jpeg" alt="AD" width="320" height="333" class="img-about">
        </div>
        <p>Walid Alrifai</p>
      </div>


    </div>

  </div>
</template>

<script>
  export default {
    name: "AboutUs",
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
    color: #42b983;
  }
  .text {
    color: dimgray;
    margin: 10px;
    font-size: 20px;

  }
  .team{
    padding-bottom: 30px;
  }
  .image-container{
    padding-bottom: 30px;
    padding-top: 30px;


  }

.col-5{
  color: #5e5e5e;

}
  .row{
    padding-bottom: 10px;
  }
  .aboutUs{
    padding-bottom: 100px;
  }

</style>