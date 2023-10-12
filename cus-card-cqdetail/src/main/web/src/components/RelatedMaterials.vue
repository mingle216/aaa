<template>
  <!--相关材料-->
        <div
        >
          <div class="title1 portal-font-color-lv1">{{ fieldName }}</div>
          <div
            v-for="(it, i) in item.fieldValue"
            :key="i + it.material_comments"
            class="tel-item"
          >
            <span v-if="!it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <span>{{ it.material_comments }}</span>
            </span>
            <span v-if="it.material_path">
              <span style="margin-right: 12px">{{ i + 1 }}.</span>
              <a
                @click="aClick"
                class="portal-primary-color-lv1"
                :download="it.material_comments"
                :href="it.material_path"
                target="_blank"
                >{{ it.material_comments }}</a
              >
            </span>
          </div>
        </div>
</template>

<script>
export default {
  props:{
    router: Object,
    item:{
        type:Object,
        default:()=>{
            return {}
        }
    },
    fieldName:String
  },
  methods: {
    aClick() {
      window.minosStataCollect.collect({
        actionType: 0,
        functionType: 1,
        actionParams: {
          pageCode: window.shell.getPageData().pageInfoEntity.pageCode,
          pageName: window.shell.getPageData().pageInfoEntity.pageName,
          cardWid: this.router.cardWid,
          cardId: this.router.cardId,
          cardName: this.router.cardName
        },
        startTime: new Date().getTime()
      });
    }
  }
}
</script>

<style lang="less" scoped>
.tel-item {
  height: 48px;
  border-bottom: 1px solid #f0f0f0;
  line-height: 48px;
  position: relative;
  &:last-child{
    border:none
  }
}
</style> 