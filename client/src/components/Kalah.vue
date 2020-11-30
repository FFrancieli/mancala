<template>
  <div class="section endsection">
    <div class="pit" id="north-side-kalah">
      <p class="pit-font kalah-seeds"> {{ amountOfSeeds }}</p>
    </div>
  </div>

</template>

<script>
export default {
  name: "Kalah",
  props: {
    amountOfSeeds: Number
  }
}
</script>
