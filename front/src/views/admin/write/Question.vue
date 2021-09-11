<template>
  <div>
    <div>
      전체<input type="radio" name="hidden" value="ALL" checked>
      비공개<input type="radio" name="hidden" value="Hidden" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return{
      status: 'ALL'
    }
  }
}
</script>

<style>

</style>