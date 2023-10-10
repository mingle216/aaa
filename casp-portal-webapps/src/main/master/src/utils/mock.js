import { getPageView } from '../api/service'
export const getPageDatas = async(pageCode) => {
  const [res] = await getPageView({
    params: {
      pageCode
    }
  })
  return res.data || {}
}



