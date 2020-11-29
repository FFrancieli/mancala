<template>
  <div class="board">
    <div class="section endsection">
      <div class="pit" id="mb">
      </div>
    </div>
    <div class="section midsection">
      <div class="midrow topmid">
        <div class="pit" id="pt1">
        </div>
        <div class="pit" id="pt2">
        </div>
        <div class="pit" id="pt3"></div>
        <div class="pit" id="pt4"></div>
        <div class="pit" id="pt5"></div>
        <div class="pit" id="pt6"></div>
      </div>
      <div class="midrow botmid">
        <div class="pit" id="pb6"></div>
        <div class="pit" id="pb5"></div>
        <div class="pit" id="pb4"></div>
        <div class="pit" id="pb3"></div>
        <div class="pit" id="pb2"></div>
        <div class="pit" id="pb1"></div>
      </div>
    </div>
    <div class="section endsection">
      <div class="pit" id="mt"></div>
    </div>
  </div>


</template>

<script>
export default {
  name: 'Board',
}
</script>
