<template>
  <div>
    <Node
      v-for="item in treeData"
      :key="item.categoryWid"
      :data="item"
      :isDept="isDept"
      :deptIconType="deptIconType"
      :otherClassIcon="otherClassIcon"
    ></Node>
  </div>
</template>

<script>
import Node from "./Node";
import { clickTrack } from "../mixins/track.js";
export default {
  name: "CategoryTree",
  props: ["treeData", "router", "deptIconType", "otherClassIcon", "isDept"],
  mixins: [clickTrack],
  components: {
    Node,
  },
  methods: {
    handleSelect(curr) {
      this.$emit("change-node", curr);
    },
    handleExpanded() {
      this.handleClickTrack(); // 点击埋点
    },
  },
};
</script>
