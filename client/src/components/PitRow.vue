<template>
  <div class="midrow">
    <div class="pit" :class="isPlayerTurn ? 'clicable-row' : '' " v-for="pit in pits" :key="pit.index">
      <p class="pit-seeds pit-font">{{ pit.totalSeeds }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "PitRow",
  props: {
    currentPlayer: String,
    assignedToPlayer: {
      type: Object,
      required: true
    },
    pits: {
      type: Array,
      required: true
    }
  },
  computed: {
    isPlayerTurn() {
      return this.assignedToPlayer === this.currentPlayer;
    }
  }
}
</script>
