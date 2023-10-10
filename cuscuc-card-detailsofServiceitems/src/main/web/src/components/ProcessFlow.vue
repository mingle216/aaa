<template>
  <!--办理流程-->
  <div>
    <div class="title1 portal-font-color-lv1">
      {{ fieldName }}
    </div>
    <div class="detail-wrapper" v-html="item.fieldValue.comments" @click="aClick"></div>
<!--    <div-->
<!--      v-if="item.fieldValue.image"-->
<!--      class="lct-btn portal-font-color-lv1 portal-primary-color-hover-lv1 portal-primary-border-color-hover-lv1"-->
<!--      @click="showLct()"-->
<!--    >-->
<!--      {{ $Lan(lanFunName, "showLct", "查看流程图") }}-->
<!--    </div>-->
    <div v-if="item.fieldValue.image">
			<we-image
				ref="img"
				style="width: 100px; height: 100px"
				:src="item.fieldValue.image"
				:preview-src-list="srcList"
				@click="showLct"
				@error="handleError"
				@close="toggle"
			>
			</we-image>
<!--      <transition name="fade">-->
<!--        <div @click="toggle" v-show="lctshow" class="img-bg">-->
<!--          <div class="t-title"></div>-->
<!--          <div class="t-content">-->
<!--            <div class="xx">-->
<!--              <i class="we-icon-close xxx"></i>-->
<!--            </div>-->
<!--            <img :src="item.fieldValue.image" alt />-->
<!--          </div>-->
<!--          <div class="t-bottom"></div>-->
<!--        </div>-->
<!--      </transition>-->
    </div>
  </div>
</template>

<script>
export default {
  props: {
    router: Object,
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
    fieldName:String
  },
  data() {
    return {
      // lctshow: false,
			srcList: [this.item.fieldValue.image],
			errorImg: window.shell.ErrorImg
    };
  },
  methods: {
    aClick(event) {
      if(event.path.some(v=> v.tagName === 'A')) {
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
    },
    showLct() {
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
			this.$nextTick(() => {
				const node = this.$el.querySelector('.we-image-viewer__canvas')
				node.addEventListener('mousedown', () => {
					const _dragHandler = this.$refs.img.$children[0]._dragHandler
					document.removeEventListener('mousemove', _dragHandler)
				}, true)
			})
      // this.lctshow = true;
    },
    toggle() {
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
      // console.log(1);
      // this.lctshow = !this.lctshow;
    },
		handleError(e) {
			let img = e.srcElement
			img.src = this.errorImg
			img.onerror = null
		}
  },
};
</script>

<style lang="less" scoped>
.lct-btn {
  background: #ffffff;
  border-radius: 3px;
  // font-family: PingFangSC-Medium;
  font-size: 14px;
  border: 1px solid #d9d9d9;
  letter-spacing: 0;
  width: 110px;
  height: 36px;
  text-align: center;
  line-height: 36px;
  box-sizing: border-box;
  cursor: pointer;
  margin-top: 20px;
  overflow: hidden;
  position: relative;
}
.img-bg {
  z-index: 2000;
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  right: 0;
  left: 0;
  background: rgba(0, 0, 0, 0.7);
  opacity: 1;
}
.xx {
  cursor: pointer;
  position: absolute;
  top: 8px;
  right: 8px;
}
.xxx {
  font-size: 36px;
  font-weight: 600;
  color: #fff;
  opacity: 0.7;
}
.detail-wrapper {
  line-height: 150%;
}
.t-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  img {
    position: relative;
    // top: 40px;
    max-width: calc(100% - 80px);
    max-height: calc(100% - 80px);
  }
}
</style>
