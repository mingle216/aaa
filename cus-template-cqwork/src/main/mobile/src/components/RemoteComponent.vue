<template>
  <div>
    <div ref="container"></div>
  </div>
</template>
<script>
import ModuleLoader from '../utils/module-loader'
const mloader = new ModuleLoader()
export default {
  props: {
    url: String,
  },
  mounted () {
    this.$nextTick(() => {
      this.loadComponent()
    })
  },
  beforeDestroy () {
    if (this.vm && this.vm.$destroy && typeof this.vm.$destroy === 'function') {
      this.vm.$destroy()
    }
  },
  methods: {
    loadComponent () {
      mloader.load(this.url).then(module => {
        window.shell.emit("card-loaded")
        
        if (!module) {
           console.log('异常',this.url)
          return
        }
        try {
          if (module && module.bootstrap && typeof module.bootstrap === 'function') {
            const obj = {}
            for (let attr in this.$attrs) {
              Object.defineProperty(obj, attr, {
                get: () => this.$attrs[attr]
              })
            }
            const vm = module.bootstrap(this.$refs.container, { props: obj })
            // event handlers
            const listeners = this.$listeners
            Object.keys(listeners).forEach(key => {
              vm.$on(key, listeners[key])
            })
            this.vm = vm
          } else {
            console.error('远程组件未提供 bootstrap 入口')
          }
        } catch (e) {
          console.error(e)
          console.error('未找到应用入口')
        }
      })
    }
  }
}
</script>