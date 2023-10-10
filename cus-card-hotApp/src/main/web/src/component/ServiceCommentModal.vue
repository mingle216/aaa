<template>
  <div>
    <we-dialog
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :append-to-body="true"
      :visible.sync="manageDialogshow"
      :width="'610px'"
    >
      <template #title>
        <h3 class="dialog-title portal-font-color-lv1">
          {{ $Lan(lanFunName, "evaluateTitle", "评价") }}
        </h3>
      </template>
      <ContainerScroll
        :barKeepShow="true"
        style="height:520px"
        v-loading="loading"
      >
        <div
          class="comment-header portal-font-color-lv1"
          :class="{ border: isSubmited || list.length }"
        >
          <span
            class="iconfont icon-yipingjia commentedImg"
            v-if="isSubmited"
          />
          <div class="service-name ellipsis">{{ serviceName }}</div>
          <div class="common-flex">
            <we-rate
              class="readRate"
              v-model="avgRate"
              disabled
              show-score
              disabled-void-color="#D9D9D9"
              text-color="#FFBC00"
              :score-template="
                `{value} ${$Lan(lanFunName, 'replyScore', '分')}`
              "
            >
            </we-rate>
            <div class="portal-font-color-lv3" style="margin-left:10px">
              {{
                $Lan(lanFunName, "evaluateTotal", `${totalScore}人评价`, {
                  total: totalScore,
                })
              }}
            </div>
          </div>
        </div>
        <div v-if="!isSubmited && !loading" style="padding: 20px">
          <div style="text-align:center">
            <div class="portal-font-color-lv3">
              {{ $Lan(lanFunName, "evaluateScore", "请给服务打分") }}
            </div>
            <div class="optRate">
              <we-rate
                v-model="userRate"
                void-color="#D9D9D9"
                text-color="#FFBC00"
                void-icon-class="we-icon-star-on"
                show-score
                :score-template="
                  `{value} ${$Lan(lanFunName, 'replyScore', '分')}`
                "
              >
              </we-rate>
            </div>
          </div>
          <div>
            <we-input
              type="textarea"
              :placeholder="
                $Lan(
                  lanFunName,
                  'evaluatePlaceholder',
                  '请输入评价内容（选填）'
                )
              "
              v-model="userComment"
              :rows="3"
              :style="'margin: 10px 0 0 0'"
              maxlength="500"
              resize="none"
              show-word-limit
            >
            </we-input>
          </div>
          <div class="common-flex" style="margin-top:16px">
            <span class="portal-font-color-lv3">{{
              $Lan(
                lanFunName,
                "evaluateUploadTip",
                "可上传png或jpg格式图片，单张大小不超过500k，最多上传3张"
              )
            }}</span>
            <we-upload
              ref="Upload"
              class="upload-demo"
              action=""
              :auto-upload="false"
              :limit="3"
              :file-list="uploadLists"
              :show-file-list="false"
              :on-change="handleChangeUpload"
              :on-exceed="handleUploadExceed"
              v-show="uploadLists.length < 3"
            >
              <span
                class="portal-primary-color-lv1"
                style="margin-left:13px;cursor:pointer"
                ><span
                  class="iconfont icon-shangchuantupian"
                  style="margin-right:5px;vertical-align:middle;"
                ></span
                >{{ $Lan(lanFunName, "evaluateUpload", "点击上传") }}</span
              >
            </we-upload>
          </div>
          <div class="uploadPicLists" v-if="uploadLists.length > 0">
            <div
              class="uploadPic"
              v-for="(item, index) in uploadLists"
              :key="index"
            >
              <img class="img" :src="getImgUrl(item)" />
              <div class="uploadPic-hover">
                <span
                  class="iconfont icon-shanchu"
                  @click="handleDeletePic(index)"
                ></span>
              </div>
            </div>
          </div>
          <we-button
            size="medium"
            type="primary"
            class="portal-primary-backgroundcolor-lv1 portal-primary-border-color-lv1"
            :loading="okLoading"
            @click="saveAppraiseWithPic"
            style="margin-top:20px"
            >{{ $Lan(lanFunName, "evaluateOk", "提交评价") }}</we-button
          >
        </div>
        <div
          class="comment-filter"
          v-if="filterInfo.allCount && displayType == 0"
        >
          <we-radio-group
            v-model="scoreLevel"
            @change="queryAppraiseByPage(true)"
          >
            <we-radio :label="0"
              ><span class="portal-font-color-lv2"
                >{{ $Lan(lanFunName, "evaluateRadioAll", "全部") }}({{
                  filterInfo.allCount
                }})</span
              ></we-radio
            >
            <we-radio :label="1"
              ><span class="portal-font-color-lv2"
                >{{ $Lan(lanFunName, "evaluateRadioGood", "好评") }}({{
                  filterInfo.goodCount
                }})</span
              ></we-radio
            >
            <we-radio :label="2"
              ><span class="portal-font-color-lv2"
                >{{ $Lan(lanFunName, "evaluateRadioAverage", "中评") }}({{
                  filterInfo.middleCount
                }})</span
              ></we-radio
            >
            <we-radio :label="3"
              ><span class="portal-font-color-lv2"
                >{{ $Lan(lanFunName, "evaluateRadioBad", "差评") }}({{
                  filterInfo.badCount
                }})</span
              ></we-radio
            >
          </we-radio-group>
        </div>
        <div class="comment-content" v-if="list.length > 0">
          <div
            v-for="item in list"
            :key="item.wid"
            class="comment-content-item"
          >
            <div class="comment-content-item-title  portal-font-color-lv3">
              <!-- 评分 -->
              <div class="common-flex">
                <span>{{ item.userName }}</span>
                <we-rate
                  class="readRate"
                  :value="item.score"
                  :style="'margin-left: 8px;'"
                  disabled
                  disabled-void-color="#D9D9D9"
                  text-color="#FFBC00"
                >
                </we-rate>
                <!-- 我的评价 -->
                <span
                  class="comment-content-tag"
                  v-if="item.userId === userInfo.userAccount"
                  >{{ $Lan(lanFunName, "evaluateMy", "我的评价") }}</span
                >
              </div>
              <div class="comment-content-item-title-time">
                {{ item.createTime }}
              </div>
            </div>
            <!-- 评论 -->
            <div
              v-if="item.content || item.isDeleted === '1'"
              class="comment-content-item-content"
            >
              {{
                item.isDeleted === "1"
                  ? $Lan(lanFunName, "commentHasHidden", "该评价已被管理员隐藏")
                  : item.content
              }}
            </div>
            <!-- 图片 -->
            <div
              class="uploadPicLists"
              v-if="item.appraisePics && item.isDeleted !== '1'"
            >
              <div
                class="uploadPic"
                v-for="(item, index) in item.appraisePics"
                :key="index"
                @click="handlePreview(item)"
              >
                <img class="img" :src="item" />
              </div>
            </div>
            <!-- 回复 -->
            <div
              class="comment-reply"
              v-if="item.adminComment && item.isDeleted !== '1'"
            >
              <div class="comment-content-item-title  portal-font-color-lv3">
                <div>
                  {{ $Lan(lanFunName, "replyUser", "管理员回复：") }}
                </div>
                <div class="comment-content-item-title-time">
                  {{ item.adminComment.replyTime }}
                </div>
              </div>
              <div>
                {{ item.adminComment.replyDetail }}
              </div>
            </div>
          </div>
        </div>
        <EmptyCon
          :tip="$Lan(lanFunName, 'noData', '暂无数据')"
          :height="300"
          v-if="!list.length && isSubmited && !loading"
          mainTipClass="portal-font-color-lv3"
        ></EmptyCon>
        <div class="common-flex pageInfo" v-if="page.total">
          <div class="portal-font-color-lv3">
            {{
              $Lan(
                lanFunName,
                "pageInfo",
                "共 {total} 条，显示第 {start} ~ {end} 条",
                {
                  total: page.total,
                  start: pageStart,
                  end: pageEnd,
                }
              )
            }}
          </div>
          <we-pagination
            background
            size="medium"
            layout="prev, pager, next"
            :total="page.total"
            :current-page="page.pageNum"
            :page-size="page.pageSize"
            @current-change="handlePageChange"
          >
          </we-pagination>
        </div>
      </ContainerScroll>
      <we-image
        ref="PreviewImg"
        :src="previewImg"
        :preview-src-list="[previewImg]"
        style="width:0.01px;height:0.01px"
        :z-index="2020"
      ></we-image>
    </we-dialog>
  </div>
</template>

<script>
import { clickTrack } from "../mixins/track.js";
export default {
  props: ["router"],
  mixins: [clickTrack],
  data() {
    return {
      serviceName: "",
      serviceWid: "",
      manageDialogshow: false,
      lanFunName: "public",
      showModel: true,
      userRate: 5,
      userComment: "",
      displayType: 0,
      avgRate: 5,
      totalScore: 0,
      isSubmited: false,
      list: [],
      page: {
        total: 0,
        pageSize: 10,
        pageNum: 1,
      },
      okLoading: false,
      uploadLists: [],
      scoreLevel: 0,
      filterInfo: {
        allCount: 0,
        goodCount: 0,
        middleCount: 0,
        badCount: 0,
      },
      userInfo: window.shell.getUserInfo(),
      previewImg: "",
    };
  },
  computed: {
    pageStart() {
      return (this.page.pageNum - 1) * this.page.pageSize + 1;
    },
    pageEnd() {
      return this.page.total <= this.page.pageNum * this.page.pageSize
        ? this.page.total
        : this.page.pageNum * this.page.pageSize;
    },
  },
  methods: {
    handlePageChange(page) {
      this.page.pageNum = page;
      this.queryAppraiseByPage();
    },
    handlePreview(url) {
      this.previewImg = url;
      this.$refs.PreviewImg.showViewer = true;
    },
    handleChangeUpload(files) {
      const file = files.raw;
      const isPNG = file.type === "image/png" || file.type === "image/jpeg";
      const isLimit = file.size / 1024 < 500;
      if (!isPNG) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan(
            "public",
            "uploadTypeError",
            "请上传png或jpg格式图片"
          ),
        });
        this.uploadLists = this.uploadLists.concat();
        return false;
      }
      if (!isLimit) {
        window.shell.showMessage({
          type: "warning",
          message: this.$Lan("public", "uploadSizeError", "单张大小不超过500k"),
        });
        this.uploadLists = this.uploadLists.concat();
        return false;
      }
      this.uploadLists.push(file);
    },
    handleUploadExceed() {
      window.shell.showMessage({
        type: "warning",
        message: this.$Lan("public", "uploadExceed", "最多上传3张"),
      });
    },
    handleDeletePic(index) {
      this.uploadLists.splice(index, 1);
    },
    getImgUrl(item) {
      if (typeof item === "string") {
        return item;
      }
      return window.URL.createObjectURL(item);
    },
    async getAppraiseSummary() {
      this.loading = true;
      await window.shell
        .getServiceAppraiseSummary({ appId: this.serviceWid }, (res) => {
          this.loading = false;
          if (res.errcode === "0" && res.data && res.data.appraiseSummary) {
            this.isSubmited = res.data.isAppraised;
            this.avgRate = parseFloat(
              res.data.appraiseSummary.avgScore.toFixed(1)
            );
            this.totalScore = res.data.appraiseSummary.total || 0;
          }
          this.displayType = res.data?.displayType || 0;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    async queryAppraiseByPage(isInit) {
      if (isInit) {
        this.page.pageNum = 1;
      }
      await window.shell.queryAppraiseByPageNew(
        {
          appId: this.serviceWid,
          pageSize: this.page.pageSize,
          pageNum: this.page.pageNum,
          scoreLevel: this.scoreLevel,
        },
        (res) => {
          if (res.errcode === "0" && res.data && res.data.data) {
            const lists = (res.data.data.records || []).map((item) => ({
              ...item,
              adminComment: item.adminComment && JSON.parse(item.adminComment),
              appraisePics: item.appraisePics && JSON.parse(item.appraisePics),
            }));
            this.list = lists;
            this.page.total = res.data.data.total;
            this.filterInfo = {
              allCount: res.data.allCount || 0,
              goodCount: res.data.goodCount || 0,
              middleCount: res.data.middleCount || 0,
              badCount: res.data.badCount || 0,
            };
          }
        }
      );
    },
    async saveAppraiseWithPic() {
      this.okLoading = true;
      this.handleClickTrack({
        infoType: 0,
        serviceId: this.serviceWid,
        fucType: 4,
      });
      await window.shell
        .saveAppraiseWithPic(
          {
            uploadLists: this.uploadLists,
            data: JSON.stringify({
              appId: this.serviceWid,
              score: this.userRate,
              content: this.userComment.trim(),
            }),
          },
          (res) => {
            this.okLoading = false;
            if (res.errcode === "0") {
              this.getAppraiseSummary();
              this.queryAppraiseByPage(true);
            }
          }
        )
        .catch(() => {
          this.okLoading = false;
        });
    },
    show(serviceInfo) {
      this.serviceWid = serviceInfo.wid;
      this.serviceName = serviceInfo.serviceName;
      this.manageDialogshow = true;
      this.avgRate = 5;
      this.userRate = 5;
      this.list = [];
      this.loading = false;
      this.okLoading = false;
      this.userComment = "";
      this.scoreLevel = 0;
      this.uploadLists = [];
      this.$refs.Upload?.clearFiles();
      this.getAppraiseSummary();
      this.queryAppraiseByPage(true);
    },
  },
};
</script>

<style lang="less" scoped>
.common-flex {
  display: flex;
  align-items: center;
}
.pageInfo {
  padding: 15px 20px 20px;
  justify-content: space-between;
}
.img {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  object-fit: cover;
}
.comment-header {
  position: relative;
  width: 100%;
  background: url("../assets/images/comment-bg.png") no-repeat center;
  background-size: 100% 100%;
  padding: 20px;
  .service-name {
    font-weight: bold;
    font-size: 18px;
    line-height: 26px;
    margin-bottom: 7px;
  }
  .commentedImg {
    position: absolute;
    top: 10px;
    right: 28px;
    color: #ff9000;
    font-size: 64px;
  }
}
.uploadPicLists {
  margin-top: 6px;
  display: flex;
}
.uploadPic {
  width: 90px;
  height: 90px;
  margin-right: 8px;
  border-radius: 4px;
  cursor: pointer;
  position: relative;
  .uploadPic-hover {
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border-radius: 4px;
    background: linear-gradient(0deg, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5));
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: #fff;
  }
  &:hover .uploadPic-hover {
    display: flex;
  }
}
.readRate {
  display: flex;
  align-items: center;
  /deep/.we-rate__icon {
    font-size: 16px;
    margin-right: 0px;
  }
  /deep/.we-rate__text {
    font-weight: bold;
    margin-left: 6px;
  }
}
.optRate {
  margin: 8px auto 16px;
  /deep/.we-rate {
    height: 26px;
    .we-rate__icon {
      font-size: 26px;
      margin-right: 2px;
    }
    .we-rate__text {
      font-size: 16px;
      font-weight: bold;
      margin-left: 6px;
      display: inline-block;
      margin-top: 3px;
    }
  }
}
.comment-filter {
  padding: 13px 20px;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}
.comment-content {
  padding: 0 20px;

  &-item {
    padding: 16px 0;
    line-height: 22px;
    box-shadow: inset 0 -1px 0 0 #f0f0f0;
  }
}

.comment-content-tag {
  border: 0.5px solid #ff9000;
  background: #fff4e5;
  border-radius: 17px;
  font-size: 12px;
  height: 20px;
  line-height: 20px;
  color: #ff9000;
  padding: 0 6px;
  margin-left: 8px;
}

.comment-content-item-title {
  display: flex;
  align-items: center;
  justify-content: space-between;

  &-time {
    font-size: 12px;
  }
}
.comment-content-item-content {
  margin-top: 8px;
}

.comment-reply {
  margin-top: 11px;
  background: #f5f5f5;
  border-radius: 4px;
  padding: 15px 16px;
  line-height: 22px;
  position: relative;
  &::after {
    content: "";
    position: absolute;
    border: 21px solid;
    border-color: transparent transparent #f5f5f5 transparent;
    left: 20px;
    top: -27px;
  }
}

.dialog-title {
  font-size: 18px;
  line-height: 26px;
  font-weight: bold;
}
/deep/ .we-textarea__inner {
  font-family: "微软雅黑";
  padding: 12px;
  height: 110px;
  border: 0.5px solid #d9d9d9;
}
/deep/ .we-dialog__header {
  box-shadow: inset 0 -1px 0 0 #f0f0f0;
  height: 66px;
  display: flex;
  align-items: center;
  padding: 10px 20px;
}
/deep/ .we-dialog__headerbtn {
  top: 24px !important;
}
/deep/ .we-dialog__body {
  padding: 0;
}
/deep/.we-rate:active,
/deep/.we-rate:focus {
  outline: none;
}
/deep/.we-radio {
  margin-right: 16px;
}
/deep/.we-image-viewer__close {
  color: #ffffff;
}
</style>
