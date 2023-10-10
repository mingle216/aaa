<template>
  <div>
    <div class="cus-footer">
      <div class="content">
        <div>
          <p class="title">
            {{ $Lan(" ", "lxfs", "联系方式") }}
          </p>
          <p class="view-content">
            {{
              $Lan(
                "CUS_TEMPLATE_OFFICIAL",
                "dizhi",
                "海甸校区：海南省海口市美兰区人民大道58号"
              )
            }}
          </p>
          <p class="view-content">
            {{
              $Lan("CUS_TEMPLATE_OFFICIAL", "dizhi2", "城西校区：海南省海口市龙华区学院路4号")
            }}
          </p>
          <p class="view-content">
            {{
              $Lan("CUS_TEMPLATE_OFFICIAL", "dizhi3", "儋州校区：海南省儋州市宝岛新村")
            }}
          </p>
          <p class="view-content">
            {{
              $Lan("CUS_TEMPLATE_OFFICIAL", "dizhi4", "观澜湖校区：海口市龙华区龙桥镇羊山大道")
            }}
          </p>
        </div>
        <div class="right-box">
          <img class="logo" :src="logoUrl" alt="" />
          <div class="ctx-box">
            <img class="wxcode-img" :src="require('../../assets/images/wxcode.png')" alt="">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "",
  props: ["pageData"],
  data() {
    return {
      todayPv: 0,
      onlineUv: 0,
      pv: 0,
      wxfill: "#00CE81",
      wbfill: "#FD5C5C",
    };
  },
  computed: {
    logoImgSrc() {
      return this.pageData
        ? JSON.parse(JSON.parse(this.pageData.showProgrammeEntity.templateConfig || ""))
            .configLogo
        : {};
    },
    logoUrl() {
      // const sColor = (this.logoImgSrc && this.logoImgSrc.logoUrl) || "";
      const bColor = (this.logoImgSrc && this.logoImgSrc.whiteLogoUrl) || "";
      let url = bColor;
      url = process.env.NODE_ENV === "development" ? url.split(".cn")[1] : url;
      return url;
    },
  },
  mounted() {
    // window.shell.execTemplateMethod("accessStatisc", {}, (res) => {
    //   if (res.errcode === "0") {
    //     const { todayPv, onlineUv, pv } = res.data;
    //     this.todayPv = todayPv || 0;
    //     this.onlineUv = onlineUv || 0;
    //     this.pv = pv || 0;
    //   }
    // });
  },
};
</script>
<style scoped lang="less">
.cus-footer {
  height: 260px;
  position: relative;
  color: #fff;
  background: url(../../assets/images/footerbg.png) no-repeat;
  background-size: cover;
  background-position: center bottom;
  &::after{
    content: "";
    display: block;
    position: absolute;
    width: 100;
  }
  .content {
    display: flex;
    justify-content: space-between;
    width: 1200px;
    margin: 0 auto;
    height: 100%;
    align-items: center;
    // padding-top: 40px;
    .title {
      font-size: 20px;
      font-weight: 700;
      margin-bottom: 32px;
    }
    .view-content {
      font-size: 14px;
      line-height: 32px;
    }

    .right-box {
      display: flex;
      // flex-direction: column;
      align-items: center;
      height: 100%;
      .logo {
        height: 67px;
        width: 221px;
      }

      .wxcode-img{
        width: 122px;
        height: 122px;
        margin-left: 80px;
      }
      .ctx-box {
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex: 1;

        & > div {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          font-size: 14px;
          color: #fff;
          margin-right: 18px;
          cursor: pointer;
          position: relative;

          &:last-of-type {
            margin-right: 0;
          }
          .icon {
            width: 40px;
            height: 40px;
          }
          span {
            margin-top: 8px;
          }

          .icon-box {
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #fff;
            border-radius: 50%;
          }

          .code {
            width: 130px;
            position: absolute;
            left: 50%;
            top: -140px;
            max-width: none;
            transform: translateX(-50%);
            display: none;
          }

          &:hover {
            .icon-box {
              background-color: #276eed;
            }
            .code {
              display: block;
            }
          }
        }
      }
    }
  }
}
</style>
