<template>
  <div>
 
      <!--其他字段-->
      <!--1 文本-->
      <div v-if="item.type == 1 && itemCanSee(item.fieldValue)">
        <div class="title1 portal-font-color-lv1">
          {{ item.fieldName  }}
        </div>
        <div>{{ item.fieldValue }}</div>
      </div>
      <!--3 富文本-->
      <div v-else-if="item.type == 3 && itemCanSee(item.fieldValue)">
        <div class="title1 portal-font-color-lv1">
          {{ item.fieldName }}
        </div>
        <p class="detail-wrapper" v-html="item.fieldValue"></p>
      </div>
      <!--附件-->
      <div v-else-if="item.type == 4 && itemCanSee(item.fieldValue)">
        <div class="title1 portal-font-color-lv1">{{ item.fieldName }}</div>
        <div
          v-for="(it, i) in item.fieldValue"
          :key="i + it.material_comments"
          class="tel-item"
        >
          <span v-if="!it.material_path">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            {{ it.material_comments }}
          </span>
          <span v-if="it.material_path">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            <a
              class="portal-primary-color-lv1"
              :href="it.material_path"
              target="_blank"
              >{{ it.material_comments }}</a
            >
          </span>
        </div>
      </div>
      <!--文本 + 附件-->
      <div v-else-if="item.type == 5 && itemCanSee(item.fieldValue)">
        <div class="title1 portal-font-color-lv1">
          {{ item.fieldName }}
        </div>
        <div style="margin-bottom: 15px">{{ item.fieldValue.text }}</div>
        <div
          v-for="(it, i) in item.fieldValue.files"
          :key="i + it.file"
          class="tel-item"
        >
          <span v-if="!it.file">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            {{ it.name }}
          </span>
          <span v-if="it.file">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            <a
              class="portal-primary-color-lv1"
              :href="it.file"
              target="_blank"
              >{{ it.name }}</a
            >
          </span>
        </div>
      </div>
      <!--富文本 + 附件-->
      <div v-else-if="item.type == 6 && itemCanSee(item.fieldValue)">
        <div class="title1 portal-font-color-lv1">
          {{ item.fieldName }}
        </div>
        <p
          class="detail-wrapper"
          v-html="item.fieldValue.text"
          style="margin-bottom: 12px"
        ></p>
        <div
          v-for="(it, i) in item.fieldValue.files"
          :key="it.file + i"
          class="tel-item"
        >
          <span v-if="!it.file">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            {{ it.name }}
          </span>
          <span v-if="it.file">
            <span style="margin-right: 12px">{{ i + 1 }}.</span>
            <a
              class="portal-primary-color-lv1"
              :href="it.file"
              target="_blank"
              >{{ it.name }}</a
            >
          </span>
        </div>
      </div>
     
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      default: () => {
        return {};
      },
    },
    lanFunName: {
      type: String,
      default: "",
    },
  },
  methods:{
      itemCanSee(fieldValue){
        return  this.$parent.itemCanSee(fieldValue)
      }
  }
};
</script>

<style lang="less" scoped>
.tel-item {
  height: 48px;
  border-bottom: 1px solid #f0f0f0;
  line-height: 48px;
  position: relative;
}
.detail-wrapper{
  line-height: 150%;
}
</style>