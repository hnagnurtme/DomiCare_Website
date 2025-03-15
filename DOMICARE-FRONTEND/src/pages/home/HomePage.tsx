import IconFace from '@/assets/icons/icon-face'
import IconPeople from '@/assets/icons/icon-people'
import IconSave from '@/assets/icons/icon-save'
import { Person, pic7, pic9 } from '@/assets/images'
import CardItem from '@/components/CardItem'
import Company from './Company'
import SecctionInView from '@/components/SecctionInView'
import FeatureCard from '@/components/FeatureCard'
import { Button } from '@/components/ui/button'
import { IconGood } from '@/assets/icons/icon-good'
import { IconQuickly } from '@/assets/icons/icon-quickly'
import { IconStaff } from '@/assets/icons/icon-staff'
import { Link } from 'react-router-dom'
import { path } from '@/core/constants/path'
import CategoryProduct from '@/components/CategoryProduct'
import Comment from '@/components/Comment'

const HomePage = () => {
  return (
    <div className='w-full min-h-96 bg-secondary'>
      <div
        style={{ backgroundImage: `url('${pic7}')` }}
        className='bg-center min-h-[300px] md:min-h-[500px] lg:min-h-[800px] bg-no-repeat bg-cover'
      ></div>

      <section className='bg-bg min-h-80 flex items-center'>
        <div className='max-w-7xl mx-auto p-4'>
          <h2 className='text-head text-black text-center py-5 font-semibold '>
            Quan hệ đối tác và khách hàng đáng tự hào của chúng tôi
          </h2>
          <SecctionInView>
            <Company />
          </SecctionInView>
        </div>
      </section>
      <section className='bg-white min-h-96 flex items-center'>
        <div className='max-w-7xl mx-auto p-4'>
          <div className='flex flex-col justify-between items-center'>
            <h2 className='text-head text-black text-center py-5 font-semibold '>Những con số đáng ấn tượng</h2>
            <p className='text-sub0  text-black text-justify md:text-center '>
              Với nhiều năm kinh nghiệm trong lĩnh vực giúp việc gia đình, Domicare tự hào đang là giải pháp dọn nhà tốt
              nhất và tiện lợi nhất cho phụ nữ Việt.
            </p>

            <div className='w-full mo:w-auto flex flex-col mo:flex-row justify-between items-center gap-3 mt-10 mb-3 mo:gap-10'>
              <CardItem icon={<IconSave />} sub='Giờ làm / năm' title='600+' />{' '}
              <CardItem icon={<IconPeople />} sub='Người theo dõi' title='11K+' />
              <CardItem icon={<IconFace />} sub='Khách hàng hài lòng' title='95%' />
            </div>
          </div>
        </div>
      </section>
      <section className='bg-bg min-h-96 flex items-center'>
        <div className='max-w-7xl mx-auto p-4'>
          <div className='flex flex-col sm:flex-row gap-4 items-start sm:max-w-7xl max-w-9/12 mx-auto mt-8'>
            <FeatureCard
              icon={<IconGood />}
              title='Chuyên nghiệp - Tận tâm'
              description='Đội ngũ Tư vấn viên & Chăm sóc Khách hàng kinh nghiệm, chuyên nghiệp, tận tâm. Domicare cam kết bảo hành dịch vụ khi Khách hàng chưa hài lòng.'
            />
            <FeatureCard
              icon={<IconQuickly />}
              title='Thao tác tiện lợi và đơn giản'
              description='Tìm Người giúp việc nhà nhanh chóng qua web thao tác. Hệ thống cung cấp đầy đủ thông tin dịch vụ và Người giúp việc, tiện lợi trong việc chọn lựa công việc và đánh giá.'
            />
            <FeatureCard
              icon={<IconStaff />}
              title='Nhân viên đạt tiêu chuẩn'
              description='Nhân viên tiêu chuẩn, đăng tin chính, có đầy đủ hồ sơ. Công ty Domicare chấp nhận trách nhiệm cung cấp đào tạo và quản lý.'
            />
          </div>
          <div className='flex justify-center my-5'>
            <Button className=' text-white text-xl  px-8 rounded-md py-6 bg-main hover:bg-main/80 transition duration-300 cursor-pointer'>
              Đặt dịch vụ ngay
            </Button>
          </div>
        </div>
      </section>
      <section className='bg-white min-h-[800px] flex items-center'>
        <div className='max-w-7xl mx-auto'>
          <div className='w-full py-20 px-4'>
            <h2 className='text-head text-black text-center mb-6 md:mb-16 font-semibold '>Dịch vụ của chúng tôi</h2>
            <div className='flex  justify-end items-center'>
              <Link to={path.products} className=' text-right text-black text-sub2 py-2 hover:underline duration-200'>
                Xem tất cả
              </Link>
            </div>
            <div className='grid grid-cols-12 gap-2 md:gap-4'>
              {Array(4)
                .fill(0)
                .map((index) => {
                  return (
                    <div key={index} className='col-span-6 md:col-span-3 '>
                      <CategoryProduct
                        title='Vệ sinh bệnh viện, trường học'
                        description='Các trường học, bệnh viện cần duy trì một môi trường sạch sẽ, anh toàn. '
                        img={pic9}
                      />
                    </div>
                  )
                })}
            </div>
          </div>
        </div>
      </section>
      <section className='bg-bg md:min-h-[600px] min-h-[300px] flex items-center'>
        <div className='max-w-7xl mx-auto p-4'>
          <div className='flex flex-col justify-between items-center '>
            <h2 className='text-head text-black text-center py-5 font-semibold flex flex-col sm:flex-row gap-2 cursor-default mb-4 md:mb-10'>
              Khách hàng nói gì về <p className='text-main font-semibold'>DomiCare</p>
            </h2>

            <SecctionInView>
              <Comment />
            </SecctionInView>
          </div>
        </div>
      </section>
      <section className='bg-white min-h-[300px] md:min-h-[600px] flex items-end  '>
        <div className='max-w-7xl mx-auto p-4 h-[300px] md:h-[500px] relative'>
          <div className='grid h-full grid-cols-12 gap-4  '>
            <div className='md:col-start-5 lg:col-span-8 col-span-12 z-20 '>
              <div className='flex flex-col items-center justify-center gap-8 h-full'>
                <h2 className='text-black text-head text-center'>
                  Tại sao nên thuê <span className='text-main font-semibold'>Domicare</span> làm dịch vụ vệ sinh cho bạn
                </h2>
                <Link
                  to={path.aboutUs}
                  className='bg-brown rounded-sm text-center border duration-300 hover:bg-brown/80 uppercase text-white text-lg cursor-pointer w-[60%] py-2.5 md:py-4'
                >
                  Tìm hiểu thêm
                </Link>
              </div>
            </div>
          </div>
          <div
            className='absolute inset-0 bg-no-repeat bg-contain bg-left-bottom hidden md:block z-10'
            style={{ backgroundImage: `url(${Person})` }}
          ></div>
        </div>
      </section>
    </div>
  )
}

export default HomePage
