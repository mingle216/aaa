import Main from '@views/Home/index.vue'
import router from '../router'
import store from '@store'
import config from '@libs/config'

export const routerGo = (href = '/', title = null, stateObj = {}) => {
  window.history.pushState(stateObj, title, href)
}

// 动态完整注册路由
export const registRouter = (routerlist) => {
  return new Promise((resolve) => {
    // 注册组件,动态路由注入
    const aRouter = []
    function addComponent(routes) {
      if (!routes) return
      routes.map((it) => {
        // 注入组件,
        const { name, path, meta, children } = it
        aRouter.push({
          name,
          path,
          meta,
          component(resolve) {
            const filePath = meta.file
            const prefix = filePath.substring(0, filePath.indexOf('/'))
            const suffix = filePath.substring(
              filePath.indexOf('/') + 1,
              filePath.length
            )
            const path = `${prefix}/${suffix}`
            require([`@views/${path}`], resolve)
          }
        })

        if (children && children.length) {
          addComponent(children)
        }
      })
    }
    // 初始化执行
    addComponent(routerlist)
    // 注入路由
    router.addRoutes([
      {
        path: '/',
        name: 'home',
        redirect: `/${store.getters.redirectName}`,
        component: Main,
        meta: {
          title: '首页'
        },
        children: aRouter
      }
    ])
    // 注册成功之后,执行下面的方法
    console.log(router)
    router.onReady(function() {
      resolve()
    })
  })
}

// 转换菜单的数据格式---菜单栏后退使用
export const setMenus = (routes) => {
  const init = [config.homeName]
  function findname(routes) {
    return routes.reduce(function(prev, cur, index, arr) {
      if (cur.name && prev.indexOf(cur.name) === -1) {
        // 去除重复名称
        prev.push(cur.name)
      }
      if (cur.children && cur.children.length) {
        findname(cur.children)
      }
      return prev
    }, init)
  }
  return findname(routes)
}

/**
 * 设置一级导航路由为子集的第一个路由,初始化自动触发
 */
export const getNavList = (menus) => {
  if (!(menus && menus.length)) return
  return {
    name: menus[0].menuid,
    path: menus[0].menuid,
    title: menus[0].name
  }
}

/**
 * 扁平化处理菜单的目的：打开二级菜单 or 三级菜单页面时，此时依旧激活选中一级菜单
 * @param {扁平化处理菜单} menus
 */
export const updateFlatArray = (menus) => {
  const routes = []
  function flatArray(menus) {
    if (typeof menus === 'undefined') return
    menus.map((menu) => {
      const { children } = menu
      routes.push(menu)
      if (children && children.length) {
        flatArray(children)
      }
    })
  }
  flatArray(menus)
  return routes
}

/**
 * @param {Array} routers 路由列表数组
 * @description 用于找到路由列表中name为home的对象
 */
export const updateActiveName = (path, routes) => {
  let route = {}
  // to.path === "/" 已登录状态，解决新打开tab页面,输入login地址
  path = path === '/' ? `/${store.getters.redirectName}` : path
  routes.map((item) => {
    if (item.path === path.substring(1)) route = item
  })
  return route
}

// 递归节点 res是原始数据,parent[]是数组数据 ---针对组织结构，域与用户组，用户分类使用
export const transformTree = (res, parent) => {
  parent.map((pNode, idx) => {
    const childObj = []
    res.reduce((arr, cNode, key) => {
      cNode.pwid === pNode.wid
        ? childObj.push({
          wid: cNode.wid,
          pwid: cNode.pwid,
          title: cNode.name,
          isLeaf: false,
          data: {
            wid: cNode.wid,
            pwid: cNode.pwid,
            type: cNode.type, // 0 根节点（XXX大学） 1 一级用户分类  2 原始组织机构 3 未分配组织架构 4 自定义节点 5 自定义组织机构 6 二级分类  7 业务域  8 用户组
            code: cNode.code,
            categoryWid: cNode.categoryWid,
            orderIndex: cNode.orderIndex,
            speCharacters: cNode.speCharacters
          }
        })
        : ''
    }, [])
    // childObj = childObj.map((node, key) => {
    //   node = {
    //     ...node,
    //     isExpanded: node.type === 1 && key === 0 ? true : false,
    //     isSelected: node.type === 1 && key === 0 ? true : false
    //   };
    //   return node;
    // });
    pNode.children = childObj
    // 递归的条件,再次从原数据中查询
    childObj.length > 0 ? transformTree(res, childObj) : ''
  })
  return parent
}

export const transformSecondTree = (res, parent) => {
  parent.map((pNode, idx) => {
    const childObj = []
    res.reduce((arr, cNode, key) => {
      cNode.pwid === pNode.wid
        ? childObj.push({
          wid: cNode.wid,
          modelWid: cNode.wid,
          pwid: cNode.pwid,
          title: cNode.name,
          ptitle: pNode.title,
          expand: true,
          selected: false
        })
        : ''
    }, [])
    pNode.children = childObj
    childObj.length > 0 ? transformSecondTree(res, childObj) : ''
  })
  return parent
}

// 重组树结构
export const structureSecondTree = (data) => {
  // 根节点
  const parent = data.filter((item) => item.pwid === '-1') || []
  // 过滤未分配的用户
  data = data.filter((it) => !it.wid.includes('unassigned_'))
  const transformData = transformSecondTree(data, parent)
  const { wid, name, children, pwid } = transformData[0]
  return [
    {
      title: name,
      wid: wid,
      expand: true,
      selected: false,
      pwid: pwid,
      children: children
    }
  ]
}

// 向上查找父节点
export const findNodeUpper = (ele, tag) => {
  if (ele.parentNode) {
    if (ele.parentNode.tagName === tag.toUpperCase()) {
      return ele.parentNode
    } else {
      return findNodeUpper(ele.parentNode, tag)
    }
  }
}

// 向下查找子节点
export const findNodeDownward = (ele, tag) => {
  const tagName = tag.toUpperCase()
  if (ele.childNodes.length) {
    let i = -1
    const len = ele.childNodes.length
    while (++i < len) {
      const child = ele.childNodes[i]
      if (child.tagName === tagName) return child
      else return findNodeDownward(child, tag)
    }
  }
}