export default {
  data() {
    return {
      currentUser: window.shell.getUserInfo()// 当前登录用户信息
    }
  },
  created () {
    window.shell.on('update-login', (data) => {
      this.currentUser = data;
    })
  },
  beforeDestroy() {
    window.shell.off('update-login')
  },
 
};
