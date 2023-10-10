<template>
  <div v-loading="loading">
    <!-- 节点名称 -->
    <div class="itemCategoryDetai__title" v-if="optRow">
      {{ optRow.categoryName }}
    </div>
    <!-- 筛选项 -->
    <div class="itemCategoryDetai__opt clear" v-if="optRow">
      <div class="itemCategoryDetai__optLeft">
        <we-input v-model="keyword" :placeholder="placeholder" :maxlength="20" @keyup.native="handleKeyUp"
          style="width: 282px">
        </we-input>
        <div
          class="itemCategoryDetai__btn ellipsis portal-primary-backgroundcolor-lv1 portal-primary-backgroundcolor-hover-lv2 mr-16"
          @click="handleInputSearch">
          {{ $Lan("SYS_CARD_SERVICEITEMCATEGORYDETAIL", "search", "搜索") }}
        </div>

      </div>
      <div class="itemCategoryDetai__sort__wrap" v-if="hasVisitSort">
        <we-checkbox v-model="checked" class="mr-16" @change="handleChangeOnline">{{
              $Lan(
                "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
                "canDealOnline",
                "可在线办理"
              )
            }}</we-checkbox>
        <we-checkbox v-if="showQuickConfig" v-model="isMobile" class="mr-16" @change="handleSearch">
          {{
                $Lan(
                  "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
                  "canDealMobile",
                  "仅移动端办理"
                )
              }}</we-checkbox>
        <!-- 默认排序 -->
        <div v-if="defaultSort" class="pointer itemCategoryDetai__sort itemCategoryDetai__sort__default"
          @click="handleChangeSort(false)" :class="[sortType ? 'unselected' : 'portal-primary-color-lv1']">
          <span class="itemCategoryDetai__sortText ellipsis">{{
            $Lan(
              "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
              "defaultSort",
              "默认排序"
            )
          }}</span>
        </div>
        <!-- 按访问量排序 -->
        <div class="pointer itemCategoryDetai__sort itemCategoryDetai__sort__visit"
          :class="[sortType ? 'portal-primary-color-lv1' : '']"
          @click="handleChangeSort(sortType === 'desc' ? 'asc' : 'desc')">
          <span class="itemCategoryDetai__sortText ellipsis">{{
            $Lan(
              "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
              "visitSort",
              "按访问量排序"
            )
          }}</span>
          <div class="itemCategoryDetai__sort__icon portal-font-color-lv3">
            <div class="itemCategoryDetai__sort-up" :class="{
                'portal-primary-color-lv1': sortType === 'asc',
              }" @click.stop="handleChangeSort('asc')"></div>
            <div class="itemCategoryDetai__sort-down" :class="{
                'portal-primary-color-lv1': sortType === 'desc',
              }" @click.stop="handleChangeSort('desc')"></div>
          </div>
        </div>
      </div>
    </div>
    <template v-if="dataLists.length">
      <!-- 列表 -->
      <div style="margin-bottom: 14px;" :class="[configInfo && configInfo.hasIcon == 0 ? 'notShowImg' : '']">
        <we-table :data="dataLists" style="width: 100%"
          :header-cell-style="{background:'#F5F5F5',height:'48px',color:'#777779'}">
          <we-table-column prop="itemName" label="服务事项名称">
          </we-table-column>
          <we-table-column prop="itemDept" label="服务部门">
          </we-table-column>
          <we-table-column prop="address" label="服务主题">
            <template slot-scope="scope"><span>{{scope.row.otherCategory["1006203282087890944"]}}</span></template>
          </we-table-column>
          <we-table-column prop="roleName" label="服务角色">
            <!-- <template slot-scope="scope"><span @click="text(scope.row)">学生、教师、游客</span></template> -->
          </we-table-column>
          <we-table-column prop="address" label="操作">
            <template slot-scope="scope">
              <we-button @click="openServiceDetail(scope.row)" type="text" size="small" style="color:#a40000;cursor: pointer;"
                v-if="scope.row.workGuide">办事指南</we-button>
              <span type="text" size="small" style="color:rgba(74, 74, 76, 0.5)" v-if="!scope.row.workGuide">办事指南</span>
              <span style="color:rgba(74, 74, 76, 0.5);margin:0 5px">|</span>
              <we-button v-if="scope.row.onlineServiceType !== 0 " @click="(e) => handleOnline(e, scope.row)" type="text"
                size="small" style="margin-left: 0px;color:#a40000;cursor: pointer;">在线办理</we-button>
              <span v-if="scope.row.onlineServiceType == 0 " type="text" size="small"
                style="margin-left: 0px;color:rgba(74, 74, 76, 0.5)">在线办理</span>
            </template>
          </we-table-column>
        </we-table>




        <div class="itemCategoryDetai__item" v-for="item in dataLists" :key="item.itemWid" style="display: none;">
          <img v-if="configInfo && configInfo.hasIcon == 1" class="item-img" :src="item.iconLink || errorImg"
            @error="handleError" />
          <div class="item-info">
            <div class="item-name ellipsis pointer" @click.stop="openServiceDetail(item)">
              {{ item.itemName }}
            </div>
            <!-- 按部门分类展示服务主题，其他分类展示部门名称 -->
            <div class="item-dept portal-font-color-lv3 ellipsis">
              {{
                categroyType.categoryWid === "dept-subject"
                  ? item.itemCategory
                  : item.itemDept
              }}
            </div>
          </div>
          <div class="item-button">
            <!-- 办事指南 -->
            <we-button v-if="item.workGuide" plain size="medium"
              class="portal-primary-color-lv1 portal-primary-border-color-lv1 portal-primary-backgroundcolor-hover-lv5"
              @click="openServiceDetail(item)">{{
                $Lan(
                  "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
                  "guidance",
                  "办事指南"
                )
              }}</we-button>
            <!-- 在线办理 -->
            <!-- onlineServiceType 0 不显示， 1 禁用， 2 显示 -->
            <we-button plain v-if="item.onlineServiceType !== 0" size="medium" class="ml-12" :class="[
                item.onlineServiceType == 1
                  ? 'button-disabled'
                  : 'portal-primary-color-lv1 portal-primary-border-color-lv1 portal-primary-backgroundcolor-hover-lv5',
              ]" @click="(e) => handleOnline(e, item)">{{
                $Lan(
                  "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
                  "dealOnline",
                  "在线办理"
                )
              }}</we-button>
          </div>
        </div>
      </div>
      <!-- 分页 -->
      <div class="itemCategoryDetai__page">
        <div class="total portal-font-color-lv3">
          {{
            $Lan(
              "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
              "pageTotal",
              "共 {total} 条，显示第 {start} ~ {end} 条",
              {
                total: formatNum(total),
                start: (current - 1) * pageSize + 1,
                end: current * pageSize > total ? total : current * pageSize,
              }
            )
          }}
        </div>
        <we-pagination @current-change="handleCurrentChange" :current-page="current" :page-size="pageSize"
          :background="true" layout="prev, pager, next" :total="total">
        </we-pagination>
      </div>
    </template>
    <empty-con v-else :height="300" :tip="
        $Lan(
          'SYS_CARD_SERVICEITEMCATEGORYDETAIL',
          'noServiceItem',
          '暂无相关服务事项'
        )
      "></empty-con>
  </div>
</template>

<script>
  import { clickTrack } from '../mixins/track.js';
  export default {
    props: ["optRow", "configInfo", "categroyType", "router"],
    mixins: [clickTrack],
    data() {
      const arr = (this.configInfo && this.configInfo.sortType) || [];
      return {
        errorImg: window.shell.ErrorImg,
        keyword: "",
        checked: false,
        isMobile: false,
        dataLists: [],
        total: 0,
        current: 1,
        pageSize: 10,
        sortType: arr.length == 1 && arr.includes(1) ? "desc" : false,
        loading: false,
        placeholder: window.shell.placeholderEllipsis(
          this.$Lan(
            "SYS_CARD_SERVICEITEMCATEGORYDETAIL",
            "itemPlaceholder",
            "请输入服务事项名称"
          ),
          252
        ),
      };
    },
    computed: {
      hasVisitSort() {
        const arr = (this.configInfo && this.configInfo.sortType) || [];
        // 0:默认排序 1:按访问量排序 ;两个排序方式都未勾选时，前台不展示排序组件,只勾选默认排序时，前台不展示排序组件, 只勾选按访问量排序时，前台只展示按访问量排序
        return arr.includes(1);
      },
      defaultSort() {
        const arr = (this.configInfo && this.configInfo.sortType) || [];
        return arr.includes(0) && arr.length > 1;
      },
      showQuickConfig() {
        return this.configInfo.quickConfig && this.configInfo.quickConfig[0] === 'onlyForMobile'
      }
    },
    watch: {
      optRow: {
        handler(val) {
          this.keyword = "";
          this.checked = false;
          this.isMobile = false;
          const arr = (this.configInfo && this.configInfo.sortType) || [];
          this.sortType = arr.length == 1 && arr.includes(1) ? "desc" : false;
          val ? this.handleSearch() : this.reset();
        }
      },
    },
    methods: {
      reset() {
        this.current = 1;
        this.dataLists = [];
      },
      handleError(e) {
        let img = e.srcElement;
        img.src = this.errorImg;
        img.onerror = null; //防止闪图
      },
      handleChangeOnline() {
        this.handleSearch();
        this.handleClickTrack(); // 点击埋点
      },
      handleInputSearch() {
        this.handleSearch();
        this.handleClickTrack({
          infoType: 3,
          filterInfo: {
            keyword: this.keyword.trim()
          }
        }); // 点击埋点
      },
      handleSearch() {
        this.current = 1;
        this.fetchLists();
      },
      handleKeyUp(e) {
        if (e.key === "Enter") {
          //按下回车
          this.handleInputSearch();
        }
      },
      handleCurrentChange(val) {
        this.current = val;
        this.fetchLists();
        this.handleClickTrack(); // 点击埋点
      },
      handleChangeSort(sortType) {
        this.sortType = sortType;
        this.handleSearch();
        this.handleClickTrack(); // 点击埋点
      },
      text(item) {
        console.log(item)
      },
      handleOnline(e, item) {
        console.log(e)
        // if (item.onlineServiceType == 1) {
        //   window.shell.showMessage({
        //     type: 'warning',
        //     message: '暂无使用权限，请联系管理员'
        //   })
         // return
        // }
        this.handleClickTrack({
          infoType: 1,
          itemId: item.itemWid,
          fucType: 1
        }); // 点击埋点
        let parent = e.target;
        console.log(parent)
        let name = parent.nodeName;
        while (name !== "BUTTON") {
          parent = parent.parentNode;

          if (parent) {
            name = parent.nodeName;
          }
        }
        window.shell.openOnlineDeal(
          {
            ...item,
            wid: item.itemWid,
            name: item.itemName,
          },
          parent
        );
      },
      openServiceDetail(item) {
        this.handleClickTrack({
          infoType: 1,
          itemId: item.itemWid,
          fucType: 0
        }); // 点击埋点
        window.shell.openServiceItem({
          ...item,
          wid: item.itemWid,
          name: item.itemName,
        });
      },
      fetchLists(noLoading) {
        if (!noLoading) {
          this.loading = true;
        }
        window.shell.execCardMethod(
          {
            cardId: this.configInfo && this.configInfo.cardId,
            cardWid: this.configInfo && this.configInfo.cardWid,
            method: "queryItemByCategoryList",
            param: {
              isRelate: "0",
              roleWid: this.configInfo && this.configInfo.roleWid,
              categoryWid: this.optRow && this.optRow.categoryWid,
              searchKey: this.keyword.trim().toLowerCase(),
              pageNumber: this.current,
              pageSize: this.pageSize,
              visitOrder: this.sortType,
              isAuthority: this.checked,
              isMobileOnline: this.isMobile
            },
          },
          (data) => {
            this.$emit('loadEnd')
            if (!noLoading) {
              this.loading = false;
            }
            if (data && data.errcode === "0") {
              this.dataLists = (data.data && data.data.data) || [];
              this.total = (data.data && data.data.totalSize) || 0;
            } else {
              this.dataLists = [];
              this.total = 0;
            }
          }
        );
      },
      /**
       * 格式化数字，每三位加逗号分隔
       * @param {*} value
       */
      formatNum(value) {
        let num = value;
        if (typeof num !== "number") {
          num = Number(num);
        }
        if (isNaN(num)) {
          return 0;
        }
        const reg = /(\d)(?=(\d{3})+$)/g;
        return String(num).replace(reg, "$1,");
      },
    },
  };
</script>

<style lang="less" scoped>
  .mr-16 {
    margin-right: 16px;
  }

  .margin-left {
    margin-left: 12px;
  }

  .pointer {
    cursor: pointer;
  }

  .itemCategoryDetai__opt {
    display: flex;
    align-items: center;
    margin-bottom: 16px;

    /deep/.we-checkbox__input:not(.is-checked) .we-checkbox__inner:hover,
    /deep/.we-checkbox__input:not(.is-checked).is-focus .we-checkbox__inner {
      border-color: #dcdfe6;
    }

    /deep/.we-input__inner {
      height: 36px;
      line-height: 36px;
    }
  }

  .itemCategoryDetai__optLeft {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;

    /deep/.we-checkbox__label {
      line-height: 20px;
      padding-left: 6px;
      max-width: 138px;
      vertical-align: middle;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-break: break-all;
    }
  }

  .itemCategoryDetai__title {
    font-weight: bold;
    font-size: 24px;
    line-height: 32px;
    margin-top: 2px;
    margin-bottom: 24px;
  }

  .itemCategoryDetai__btn {
    padding: 0 20px;
    height: 36px;
    line-height: 36px;
    color: #fff;
    text-align: center;
    margin-left: -2px;
    border-bottom-right-radius: 4px;
    border-top-right-radius: 4px;
    cursor: pointer;
    z-index: 1;
  }

  .itemCategoryDetai__sort__wrap {
    display: flex;
    margin-left: 58px;
    align-items: center;

    .itemCategoryDetai__sort {
      line-height: 22px;

      .itemCategoryDetai__sortText {
        max-width: 124px;
        display: inline-block;
        vertical-align: middle;
      }

      &:not(:last-child):after {
        content: "";
        position: relative;
        display: inline-block;
        vertical-align: middle;
        width: 0px;
        height: 16px;
        top: 0px;
        right: 0;
        margin: 0 9px;
        border-left: 1px solid #bfbfbf;
      }
    }

    .itemCategoryDetai__sort__visit {
      display: flex;
      align-items: center;
    }

    .itemCategoryDetai__sort__icon {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      width: 8px;
      margin-left: 4px;
    }

    .itemCategoryDetai__sort-up {
      border-width: 4px;
      border-top-width: 0;
      border-style: dashed;
      border-bottom-style: solid;
      border-left-color: transparent !important;
      border-right-color: transparent !important;
      margin-bottom: 2px;
      cursor: pointer;
    }

    .itemCategoryDetai__sort-down {
      border-width: 4px;
      border-bottom-width: 0;
      border-style: dashed;
      border-top-style: solid;
      border-left-color: transparent !important;
      border-right-color: transparent !important;
      cursor: pointer;
    }
  }

  .itemCategoryDetai__item {
    display: flex;
    align-items: center;
    padding: 19px 0;

    &:not(:last-child) {
      border-bottom: 1px #f0f0f0 solid;
    }

    .item-img {
      width: 44px;
      height: 44px;
      margin-right: 20px;
    }

    .item-info {
      flex: 1;
      min-width: 0;
    }

    .item-button {
      margin-left: 90px;
      min-width: 210px;
      flex-wrap: nowrap;
      text-align: right;

      /deep/.we-button--medium {
        padding: 10px 15px;
      }
    }

    .item-name {
      font-size: 16px;
      line-height: 24px;
    }

    .button-disabled,
    .button-disabled:hover {
      border-color: #c1cbd4;
      color: #c1cbd4;
    }

    // .item-name:focus {
    //   outline: none;
    // }
    .item-dept {
      line-height: 22px;
    }
  }

  .notShowImg {
    .itemCategoryDetai__item {
      padding: 14px 0;
    }
  }

  .itemCategoryDetai__page {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .total {
      margin-right: 10px;
      font-size: 12px;
    }

    /deep/.we-pagination__jump {
      margin-left: 10px;
    }
  }
</style>