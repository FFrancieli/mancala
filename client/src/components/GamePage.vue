<template>
  <div class="container">
    <div class="current-player">
      {{ currentPlayer }}'s turn
    </div>

    <Board :pits="sortPitsByIndex()"
           :current-player="currentPlayer"
           :players="players"/>

  </div>
</template>

<script>
import Board from "@/components/Board";

export default {
  name: "GamePage",
  components: {Board},
  methods: {
    sortPitsByIndex() {
      return this.pits.sort(function (a, b) {
        return a.index - b.index
      });
    }
  },
  data() {
    return {
      "currentPlayer": "Player 1",
      "players": [
        {
          "name": "Player 1",
          "orientation": "SOUTH"
        },
        {
          "name": "Player 2",
          "orientation": "NORTH"
        }
      ],
      "pits": [
        {
          "index": 0,
          "amountOfSeeds": 4
        },
        {
          "index": 1,
          "amountOfSeeds": 4
        },
        {
          "index": 2,
          "amountOfSeeds": 6
        },
        {
          "index": 3,
          "amountOfSeeds": 6
        },
        {
          "index": 4,
          "amountOfSeeds": 6
        },
        {
          "index": 5,
          "amountOfSeeds": 6
        },
        {
          "index": 6,
          "amountOfSeeds": 0
        },
        {
          "index": 7,
          "amountOfSeeds": 6
        },
        {
          "index": 8,
          "amountOfSeeds": 6
        },
        {
          "index": 9,
          "amountOfSeeds": 6
        },
        {
          "index": 10,
          "amountOfSeeds": 6
        },
        {
          "index": 11,
          "amountOfSeeds": 6
        },
        {
          "index": 12,
          "amountOfSeeds": 6
        },
        {
          "index": 13,
          "amountOfSeeds": 2
        }
      ]
    }
  }
}
</script>
