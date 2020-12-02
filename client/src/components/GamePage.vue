<template>
  <div class="container">
    <div class="current-player">
      {{ currentPlayer }}'s turn
    </div>

    <Board :current-player="currentPlayer"
           :players="players"
           :pits="pits"
           @pit-clicked="test($event)"/>

  </div>
</template>

<script>
import Board from "@/components/Board";
import gameService from '@/services/GameService'


export default {
  name: "GamePage",
  components: {Board},
  props: {
    gameId: {
      type: String,
      required: true
    },
    currentPlayer: {
      type: String,
      required: true
    },
    players: {
      type: Array,
      required: true
    },
    pits: {
      type: Array,
      required: true
    },
  },
  methods: {
    sow() {
      gameService.sowSeeds(this.gameId, 0);
    },
    test(pitId) {
      this.$emit('pit-clicked', pitId);
    }
  },
}
</script>
