import IconFb from '@/assets/icons/icon-fb'
import IconInstagram from '@/assets/icons/icon-instargram'
import IconTwitter from '@/assets/icons/icon-twitter'
import IconYtb from '@/assets/icons/icon-ytb'
import { logoSecond } from '@/assets/images'
import { path } from '@/core/constants/path'

import { Link } from 'react-router-dom'

export default function Footer() {
  return (
    <footer className='w-full  z-50 '>
      <div className='bg-mainStrong/90 w-full h-full text-white pt-10'>
        <div className='max-w-7xl mx-auto px-16 py-2 h-full '>
          <div className='grid grid-cols-12 gap-4'>
            <div className='col-span-12 md:col-span-4 lg:col-span-6 w-full'>
              <div className='flex flex-col items-start justify-center gap-3 shrink-0'>
                <Link to={path.home} className='flex justify-between items-center '>
                  <img className='w-[300px] h-auto ' src={logoSecond} alt='logoDomicare' />
                </Link>
                <p className='text-sub2 capitalize'>© 2025 Domicare. All rights Reserved.</p>
              </div>
            </div>
            <div className='col-span-12 md:col-span-8 lg:col-span-6 w-full'>
              <div className='flex flex-col md:flex-row md:justify-end items-start gap-8 ml-4'>
                <ul className=' md:basis-1/3  flex flex-col justify-center items-start md:items-end gap-1 md:gap-2'>
                  <li>
                    <h3 className='text-sub1 font-semibold md:pb-3 pb-1 cursor-pointer text-end'>Dịch vụ</h3>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end '>Khử trùng</p>
                    </Link>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>Vệ sinh</p>
                    </Link>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>Thuê giúp việc</p>
                    </Link>
                  </li>
                </ul>
                <ul className=' md:basis-1/3  flex flex-col justify-center items-start md:items-end gap-1 md:gap-2 '>
                  <li>
                    <h3 className='text-sub1 font-semibold md:pb-3 pb-1 cursor-pointer text-end'>DomiCare và bạn</h3>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>Tại sao lại là DomiCare</p>
                    </Link>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>FAQ</p>
                    </Link>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>Tuyển dụng</p>
                    </Link>
                  </li>
                  <li>
                    <Link to={'/'}>
                      <p className='hover:text-gray-300 duration-300 text-end'>Bài viết</p>
                    </Link>
                  </li>
                </ul>
                <ul className=' md:basis-1/3  flex flex-col justify-center items-start md:items-end gap-1 '>
                  <li>
                    <h3 className='text-sub1 font-semibold '>Follow us</h3>
                  </li>
                  <li>
                    <ul className='flex justify-start items-center md:flex-col gap-2 md:gap-1'>
                      <li>
                        <Link className='flex justify-end items-center' to={'/'}>
                          <IconYtb className='fill-white hover:fill-gray-300 mr-1' />
                        </Link>
                      </li>
                      <li>
                        <Link to={'/'}>
                          <IconFb className='fill-white hover:fill-gray-300' />
                        </Link>
                      </li>
                      <li>
                        <Link to={'/'}>
                          <IconTwitter className='fill-white hover:fill-gray-300' />
                        </Link>
                      </li>
                      <li>
                        <Link to={'/'}>
                          <IconInstagram className='fill-white hover:fill-gray-300' />
                        </Link>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div>
            <hr className='bg-white h-[1px] mt-10 md:mt-30' />
            <div className='py-4 flex items-center justify-center  md:justify-end gap-16 font-semibold text-white text-sub1'>
              <Link to={'/service'} className=''>
                Điều khoản và điều kiện
              </Link>
              <Link to={'/service'} className=''>
                Chính sách bảo mật
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  )
}
