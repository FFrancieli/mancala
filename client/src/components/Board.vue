<template>
  <div class="board">
    <div class="section endsection">
      <div class="pit" id="north-side-kalah">
        <p class="pit-font kalah-seeds"> {{ northOrientationKalah.amountOfSeeds }}</p>
      </div>
    </div>
    <div class="section midsection">
      <div class="midrow topmid">
        <div class="pit" v-for="pit in northSideOrientationPits" :key="pit.index">
          <p class="pit-seeds pit-font">{{ pit.amountOfSeeds }}</p>
        </div>
      </div>
      <div class="midrow botmid">
        <div class="pit" v-for="pit in southSideOrientationPits" :key="pit.index">
          <p class="pit-seeds pit-font">{{ pit.amountOfSeeds }}</p>
        </div>
      </div>
    </div>
    <div class="section endsection">
      <div class="pit" id="mt">
        <p class="pit-font kalah-seeds"> {{ southOrientationKalah.amountOfSeeds }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Board',
  data() {
    return {
      "pits": [
        {
          "pitType": "REGULAR",
          "index": 0,
          "amountOfSeeds": 4
        },
        {
          "pitType": "REGULAR",
          "index": 1,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 2,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 3,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 4,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 5,
          "amountOfSeeds": 6
        },
        {
          "pitType": "KALAH",
          "index": 6,
          "amountOfSeeds": 0
        },
        {
          "pitType": "REGULAR",
          "index": 7,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 8,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 9,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 10,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 11,
          "amountOfSeeds": 6
        },
        {
          "pitType": "REGULAR",
          "index": 12,
          "amountOfSeeds": 6
        },
        {
          "pitType": "KALAH",
          "index": 13,
          "amountOfSeeds": 2
        }
      ]
    }
  },
  computed: {
    northSideOrientationPits() {
      return this.pits.filter(pit => pit.index > 6 && pit.index !== 13)
          .sort(function (a, b) {
            return a.index - b.index
          });
    },

    northOrientationKalah() {
      return this.pits.filter(pit => pit.index === 13)[0];
    },

    southOrientationKalah() {
      return this.pits.filter(pit => pit.index === 6)[0];
    },
    southSideOrientationPits() {
      return this.pits.filter(pit => pit.index <= 5)
          .sort(function (a, b) {
            return a.index - b.index
          });
    }
  }
}
</script>
