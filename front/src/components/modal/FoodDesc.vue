<template>
  <div>
    <span>
      {{ data }}
    </span>
  </div>
</template>

<script>
export default {
  props: ["data"],
};
</script>

<style scoped>
span {
  font-size: 12px;
}
</style>
