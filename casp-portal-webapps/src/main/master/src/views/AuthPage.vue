<template>
  <div class="auth__page" v-show="visible">
    <template v-if="$_IS_MOBILE">
      <div class="no-h5__permission" v-if="siteList.length <= 1">
        <img
          src="../assets/images/no-permission.png"
          alt=""
          width="220"
          height="115"
        />
        <p style="margin-top:12px">
          {{ $Lan("public", "noAuthSites", "该内容暂无权限，请联系管理员") }}
        </p>
        <div v-if="!siteList.length">
          <!-- <p v-if="!loginUser">
            {{ $Lan("public", "please", "或请") }}
            <span class="primary-color pointer" @click="handleLogin">
              {{ $Lan("public", "login", "登录") }}
            </span>
          </p> -->
          <p v-if="loginUser">
            {{ $Lan("public", "please", "或请") }}
            <span class="primary-color pointer" @click="handleLogout">
              {{ $Lan("public", "logout", "退出登录") }}
            </span>
          </p>
        </div>
        <p v-if="siteList.length === 1">
          <span class="primary-color">{{ $Lan("public", "threeSeconds", "3秒后") }}</span>
          <span>{{ $Lan("public", "autoJump", "系统将自动跳转至您有权限的内容") }}</span>
        </p>
        <p class="link__button" v-if="siteList.length === 1" @click="handleGoNow">
          {{ $Lan("public", "goNow", "立即前往") }}
        </p>
      </div>
      <div class="mobile-sites__permission" v-else>
        <img
          src="../assets/images/no-permission.png"
          alt=""
          width="220"
          height="115"
        />
        <div>
          <p class="title">
            {{ $Lan("public", "noAuthSites", "该内容暂无权限，请联系管理员") }}
          </p>
          <p class="sub__title">
            {{ $Lan("public", "otherChoose", "您还可以选择访问以下站点：") }}
          </p>
          <ul class="site__list">
            <li class="site__item" v-for="item in siteList" :key="item.wid" @click="handleLink(item)">
              <span>{{ item.siteName }}</span>
            </li>
          </ul>
        </div>
      </div>
    </template>
    <template v-else>
      <div class="no-pc__permission" v-if="siteList.length <= 1">
        <img src="../assets/images/no-permission.png" alt="" />
        <p style="margin-top:12px">
          {{ $Lan("public", "noAuthSites", "该内容暂无权限，请联系管理员") }}
        </p>
        <div v-if="!siteList.length">
          <!-- <p v-if="!loginUser">
            {{ $Lan("public", "please", "或请") }}
            <span class="primary-color pointer" @click="handleLogin">
              {{ $Lan("public", "login", "登录") }}
            </span>
          </p> -->
          <p v-if="loginUser">
            {{ $Lan("public", "please", "或请") }}
            <span class="primary-color pointer" @click="handleLogout">
              {{ $Lan("public", "logout", "退出登录") }}
            </span>
          </p>
        </div>
        <p v-if="siteList.length === 1">
          <span class="primary-color">{{ $Lan("public", "threeSeconds", "3秒后") }}</span>
          <span>{{ $Lan("public", "autoJump", "系统将自动跳转至您有权限的内容") }}</span>
        </p>
        <p class="primary-color pointer" v-if="siteList.length === 1" @click="handleGoNow">
          {{ $Lan("public", "goNow", "立即前往") }}
        </p>
      </div>
      <div class="pc-sites__permission" v-else>
        <img src="../assets/images/no-permission.png" alt="" />
        <div style="padding-left: 36px">
          <p class="title">
            {{ $Lan("public", "noAuthSites", "该内容暂无权限，请联系管理员") }}
          </p>
          <p class="sub__title">
            {{ $Lan("public", "otherChoose", "您还可以选择访问以下站点：") }}
          </p>
          <ul class="site__list">
            <li class="site__item" v-for="item in siteList" :key="item.wid" @click="handleLink(item)">
              <span class="dot"></span>
              <span :title="item.siteName">{{ item.siteName }}</span>
            </li>
          </ul>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { getLoginUser, getUserPermissionRouters } from "../api/service";
import globalObj from "../utils/global";
export default {
  data() {
    return {
      loginUser: null,
      language: 'zh_CN',
      siteList: [],
      visible: false
    };
  },
  computed: {
    origin() {
      let origin = location.origin
      if (!origin) {
        origin =  location.protocol + "//" + location.host;
      }
      return origin
    }
  },
  methods: {
    handleLogin() {
      const page = window.location.hash.replace('#', '')
      globalObj.login({
        params: {
          localHref: page
        }
      })
    },
    handleLogout() {
      const page = window.location.hash.replace('#', '')
      globalObj.logout({
        params: {
          localHref: page
        }
      })
    },
    async getLanguage() {
      const [res] = await getLoginUser();
      if (res.errcode === "0") {
        this.loginUser = res.data;
      }
      let defaultLanKey=  navigator.language.replace('-','_')
      defaultLanKey = defaultLanKey.includes('zh') ? 'zh_CN' : defaultLanKey  // 所有中文都处理成简体中文
      this.language = this.loginUser?.preferredLanguage || defaultLanKey
    },
    async getSiteList() {
      const [res] = await getUserPermissionRouters(
        {
          params: {
            langCountry: this.language
          }
        }
      )
      if (res.errcode === "0") {
        this.siteList = res.data || []
      }
      if (this.siteList.length === 1) {
        // setTimeout(() => {
          location.href = `${this.origin}/${this.siteList[0].siteRoute}/index.html`
        // }, 3000)
      }
    },
    handleGoNow() {
      location.href = `${this.origin}/${this.siteList[0].siteRoute}/index.html`
    },
    handleLink(item) {
      location.href = `${this.origin}/${item.siteRoute}/index.html`
    }
  },
  async mounted() {
    await this.getLanguage()
    await this.getSiteList()
    await globalObj.getStaticData({
      languageKey: this.language,
      modelKey: "casp-portal",
    }).then(() => {
      this.visible = true
    })
  },
};
</script>

<style scoped>
.primary-color {
  color: #307afb;
}
.pointer {
  cursor: pointer;
}

.auth__page {
  line-height: normal;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.no-pc__permission {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;
  color: #8C8C8C;
}
.no-pc__permission p {
  margin-bottom: 8px;
}

.pc-sites__permission {
  display: flex;
  align-items: center;
}
.pc-sites__permission .title {
  font-size: 16px;
  font-weight: bold;
  color: #262626;
  letter-spacing: 0;
  line-height: 24px;
  margin-bottom: 8px;
}
.pc-sites__permission .sub__title {
  font-size: 14px;
  color: #8c8c8c;
  letter-spacing: 0;
  line-height: 22px;
  margin-bottom: 12px;
}
.pc-sites__permission .site__item {
  font-size: 14px;
  color: #307afb;
  letter-spacing: 0;
  line-height: 18px;
  margin-bottom: 12px;
  cursor: pointer;
  max-width: 240px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.pc-sites__permission .site__item .dot {
  display: inline-block;
  width: 4px;
  height: 4px;
  vertical-align: middle;
  background: #307afb;
  border-radius: 4px;
  margin-right: 5px;
}

.no-h5__permission {
  text-align: center;
  color: #9fa8b5;
}
.no-h5__permission .link__button {
  width: 200px;
  height: 36px;
  padding: 0 18px;
  margin: 16px auto 0;
  line-height: 36px;
  text-align: center;
  color: #307afb;
  border: 1px solid #307afb;
  border-radius: 4px;
}
.mobile-sites__permission {
  text-align: center;
  color: #9fa8b5;
}
.mobile-sites__permission .title,
.sub__title {
  font-size: 14px;
  color: #9fa8b5;
  letter-spacing: 0;
  line-height: 18px;
  padding-top: 8px;
}
.mobile-sites__permission .site__list {
  padding-top: 16px;
  margin: 0 auto;
  width: 200px;
}
.mobile-sites__permission .site__item {
  width: 100%;
  height: 36px;
  line-height: 36px;
  padding: 0 18px;
  border-radius: 4px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  color: #307afb;
  border: 1px solid #307afb;
  margin-bottom: 8px;
}
</style>
