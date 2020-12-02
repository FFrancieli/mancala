<template>
  <div class="midrow">
    <div class="pit"
         :id="pit.index"
         :key="pit.index"
         @click="$emit('pit-clicked', $event.target.id)"
         :class="isPlayerTurn ? 'clicable-row' : '' " v-for="pit in pits">
      <p class="pit-seeds pit-font" :id="pit.index">{{ pit.totalSeeds }}</p>
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
  },
  methods: {
    test(event) {
      this.$emit('pit-clicked', event.target.id);
    }
  }
}
</script>
