<template>
  <div class="container">
    <div class="current-player">
      {{ currentPlayer }}'s turn
    </div>

    <Board :current-player="currentPlayer"
           :players="players"
           :pits="pits"/>

  </div>
</template>

<script>
import Board from "@/components/Board";

export default {
  name: "GamePage",
  components: {Board},
  props: {
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
  }
}
</script>
