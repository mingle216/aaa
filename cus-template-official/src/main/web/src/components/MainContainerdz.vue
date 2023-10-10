<template>
        <we-main
          :class="[cardArea === 'default' ? 'main__center' : 'main__default']"
          :style="cardArea === 'default' ? { padding: calPadding } : {}"
        >
          <slot></slot>
        </we-main>
      </template>
      
      <script>
      import { isFirefox } from "../utils/util.js";
      export default {
        props: {
          cardArea: {
            type: String,
            default: "all",
          }
        },
        data() {
          return {
            pd: 600,
            isFirefox: isFirefox(),
          };
        },
        computed: {
          calPadding() {
            return this.isFirefox ? `12px ${this.pd}px 0` : `310px calc(50% - 41.5rem) 0`
          }
        },
        methods: {
          setFireFoxPadding() {
            const conEle = document.querySelectorAll(".tempalteConWrap"); // 模板主容器dom
            const conWidth =
              (conEle && conEle.length && conEle[0] && conEle[0].offsetWidth) ||
              document.body.offsetWidth;
            this.pd = conWidth / 2 - 600;
          },
        },
        mounted() {
          if (this.isFirefox && this.cardArea === 'default') {
            this.setFireFoxPadding();
            window.addEventListener("resize", this.setFireFoxPadding, false);
          }
        },
        beforeDestroy() {
          console.log("beforeDestroy");
          window.removeEventListener("resize", this.setFireFoxPadding, false);
        },
      };
      </script>
      
      <style scoped>
      .main__center {
        overflow: hidden;
        width: 0;
      }
      .main__default{
        overflow-x: hidden;
      }
      </style>