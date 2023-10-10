const APP_LOADER_NAME = '__REMOTE_LOADED_APP__'

export default class ModuleLoader {
  constructor () {
    this.requests = {}
    this.cached = {}
  }

  load (url) {
    if (!url) {
      console.warn('load modules failed: no url !')
      return Promise.resolve(null)
    }

    const { cached } = this
    if (cached[url]) {
      return Promise.resolve(cached[url])
    }

    return new Promise((resolve) => {
      const { requests } = this
      const running = requests[url] && requests[url].length > 0

      requests[url] = requests[url] || []
      requests[url].push(resolve)

      if (!running) {
        fetch(url).then(res => res.text()).catch((e) => {
          console.error(e)
          return ''
        }).then(text => {
          const module = (function moduleRunner() {
            let result = null

            Object.defineProperty(window, APP_LOADER_NAME, {
              configurable: true,
              set (value) {
                result = value
              }
            })

            try {
              const func = new Function(text)
              func()
            } catch {
              // console.error('远程模块执行错误:'+text, e)
            }

            delete window[APP_LOADER_NAME]

            return result
          })()

          if (module && module.bootstrap && typeof module.bootstrap === 'function') {
            cached[url] = module
          } else {
            cached[url] = null
          }

          const callbacks = requests[url]
          if (callbacks && callbacks.length > 0) {
            callbacks.forEach(callback => callback(cached[url]))
            requests[url] = null
          }
        })
      }
    })
  }
}
