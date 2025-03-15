import { logoSecond } from '@/assets/images'
import { path } from '@/core/constants/path'
import { Link } from 'react-router-dom'
// import { Avatar, AvatarFallback, AvatarImage } from '../ui/avatar'
import {
  NavigationMenu,
  NavigationMenuContent,
  NavigationMenuItem,
  NavigationMenuList,
  NavigationMenuTrigger
} from '../ui/navigation-menu'
import classnames from 'classnames'
import { useState } from 'react'
import IconHeadphone from '@/assets/icons/icon-headphone'
export default function Header() {
  const [isShow, setIsShow] = useState<boolean>(false)
  return (
    <header className='w-full h-10 md:h-16 z-50 relative'>
      <div className='bg-secondary w-full h-10 md:h-16'>
        <div className='max-w-7xl mx-auto px-4 py-2 h-full flex justify-center md:justify-end items-center'>
          <div className='flex justify-center items-center gap-1 cursor-pointer'>
            <IconHeadphone className='fill-black w-5 h-5  md:w-8 md:h-8' />
            <p className='text-black text-sm font-bold'> Hỗ trợ khách hàng</p>
          </div>
        </div>
      </div>
      <div className='absolute top-10 md:top-[64px] right-0 left-0 bg-white md:bg-white/80 h-[60px] md:h-[100px] z-90'>
        <div className='max-w-7xl mx-auto px-4  h-full flex justify-end items-center'>
          <div className='grid grid-cols-12 gap-4 w-full h-full'>
            <div className='col-span-8 md:col-span-3 order-2'>
              <div className='flex justify-center items-center h-full'>
                <Link to={path.home} className='flex justify-between items-center '>
                  <img className=' w-auto h-15 ' src={logoSecond} alt='logoDomicare' />
                </Link>
              </div>
            </div>
            <div className='col-span-7 order-2 hidden md:block'>
              <ul className='flex h-full items-center justify-center  gap-4 lg:gap-6 ml-4'>
                <li>
                  <NavigationMenu>
                    <NavigationMenuList>
                      <NavigationMenuItem>
                        <NavigationMenuTrigger className='!bg-transparent hover:!bg-transparent text-sm !text-tmain hover:!text-main duration-300'>
                          Dịch vụ cung cấp
                        </NavigationMenuTrigger>
                        <NavigationMenuContent>
                          <div className='w-[200px] min-h-20 py-2'>
                            <ul className='flex flex-col justify-center items-center gap-1'>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Khử trùng
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Dịch vụ vệ sinh nhà
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Dịch vụ vệ sinh nội thất
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Dịch vụ vệ sinh thảm
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Dịch vụ vệ sinh điều hoà
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Thuê giúp việc
                              </li>
                              <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                                Dịch vụ vệ sinh các cơ sở
                              </li>
                            </ul>
                          </div>
                        </NavigationMenuContent>
                      </NavigationMenuItem>
                    </NavigationMenuList>
                  </NavigationMenu>
                </li>
                <li>
                  <Link to={'/'} className='flex justify-between items-center  w-full group'>
                    <p className='text-sm text-tmain text-center line-clamp-2 group-hover:text-main duration-300'>
                      Tin tức
                    </p>
                  </Link>
                </li>
                <li>
                  <Link to={'/'} className='flex justify-between items-center  w-full group'>
                    <p className='text-sm text-tmain text-center line-clamp-2 group-hover:text-main duration-300'>
                      Tuyển dụng
                    </p>
                  </Link>
                </li>
                <li>
                  <Link to={'/'} className='flex justify-between items-center  w-full group'>
                    <p className='text-sm text-tmain text-center line-clamp-2 group-hover:text-main duration-300'>
                      Tại sao lại chọn DomiCare?
                    </p>
                  </Link>
                </li>
              </ul>
            </div>
            <div className='col-span-2 md:col-span-2 order-1 md:order-3'>
              <div className='flex h-full justify-end items-center'>
                {/* <NavigationMenu>
                  <NavigationMenuList>
                    <NavigationMenuItem>
                      <NavigationMenuTrigger className='!bg-transparent hover:!bg-transparent text-sm !text-tmain hover:!text-main duration-300 flex h-full justify-start md:justify-end items-center gap-3 group cursor-pointer !p-2 !pl-0 md:!pl-4 md:!pr-0'>
                        <div className='shrink-0 flex justify-center items-center  w-10 h-10   '>
                          <Avatar>
                            <AvatarImage
                              src='https://scontent.fsgn2-11.fna.fbcdn.net/v/t39.30808-6/475180610_584795851049457_244361787057408833_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=833d8c&_nc_ohc=RvSzXxT51ykQ7kNvgGTYoFt&_nc_oc=Adh2WswNkFbyDl5Dkub6oIF5dRBgWtiiRP8IA9Z-TLQ7VUqxeNDqsqhv6oYL_tAq-l12Gqjyv7n2I8-oCu-c_KfM&_nc_zt=23&_nc_ht=scontent.fsgn2-11.fna&_nc_gid=AMD8yIr19kdndAdQ6fbHiQc&oh=00_AYFGpO96-ETpecOqF1xiGKRqbPgrTX8IaCEw8lF4mlP5dw&oe=67D62E14'
                              alt='@shadcn'
                            />
                            <AvatarFallback>CN</AvatarFallback>
                          </Avatar>
                        </div>
                        <p className='hidden md:block text-sm text-tmain group-hover:text-main duration-300 truncate'>
                          duyaivy
                        </p>
                      </NavigationMenuTrigger>
                      <NavigationMenuContent>
                        <div className='w-[150px] min-h-10 py-2 '>
                          <ul className='flex flex-col justify-center items-center gap-1'>
                            <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                              <Link to={'/profile'}>Tài khoản của tôi</Link>
                            </li>
                            <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                              <Link to={'/history'}>Lịch sử dịch vụ</Link>
                            </li>
                            <li className='hover:bg-main/10 w-full rounded-[2px] py-1 text-center text-tmain hover:!text-main text-sm cursor-pointer'>
                              <Link to={'/logout'}>Đăng xuất</Link>
                            </li>
                          </ul>
                        </div>
                      </NavigationMenuContent>
                    </NavigationMenuItem>
                  </NavigationMenuList>
                </NavigationMenu> */}

                {/* Đăng nhập */}
                <div className='flex justify-end h-full items-center '>
                  <Link
                    to={path.login}
                    className='rounded-sm  md:px-4 mo:!px-8 text-sub2 py-2 duration-300 md:bg-main md:text-white md:hover:bg-main/90 '
                  >
                    Đăng nhập
                  </Link>
                </div>
              </div>
            </div>
            <div className='col-span-2 order-3 md:hidden'>
              <button
                onClick={() => {
                  setIsShow((prev) => !prev)
                }}
                className='h-full w-full flex justify-end items-center p-2 pr-0 cursor-pointer'
              >
                <svg className='w-5 h-5 fill-black' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 448 512'>
                  <path d='M0 96C0 78.3 14.3 64 32 64l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L32 128C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L32 288c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32L32 448c-17.7 0-32-14.3-32-32s14.3-32 32-32l384 0c17.7 0 32 14.3 32 32z' />
                </svg>
              </button>
            </div>
          </div>
        </div>
        {/* popover */}
        <div
          className={classnames(
            'absotute top-[-100%] right-0 left-0 bg-white shadow h-80px] translate-y-[-80px] opacity-0 invisible duration-300 z-10',

            { 'translate-y-[0px] opacity-100 visible ': isShow }
          )}
        >
          <ul className='flex w-full flex-col justify-center items-center py-2'>
            <li>
              <Link to={'/'} className='flex justify-between items-center  w-full group'>
                <p className='text-sm text-tmain py-2 text-center line-clamp-2 group-hover:text-main duration-300'>
                  Dịch vụ cung cấp
                </p>
              </Link>
            </li>
            <li>
              <Link to={'/'} className='flex justify-between items-center  w-full group'>
                <p className='text-sm text-tmain py-2 text-center line-clamp-2 group-hover:text-main duration-300'>
                  Tin tức
                </p>
              </Link>
            </li>
            <li>
              <Link to={'/'} className='flex justify-between items-center  w-full group'>
                <p className='text-sm text-tmain py-2 text-center line-clamp-2 group-hover:text-main duration-300'>
                  Tuyển dụng
                </p>
              </Link>
            </li>
            <li>
              <Link to={'/'} className='flex justify-between items-center  w-full group'>
                <p className='text-sm text-tmain py-2 text-center line-clamp-2 group-hover:text-main duration-300'>
                  Tại sao lại chọn DomiCare?
                </p>
              </Link>
            </li>
          </ul>
        </div>
      </div>

      {/* <div className='flex items-center gap-10'>
        <Link to='/login' className='text-[#8987A1]'>
          Sign In
        </Link>
        <Link to='/register' className='bg-[#4E47FF] text-white px-4 py-2 rounded-lg'>
          Sign Up
        </Link>
      </div> */}
    </header>
  )
}
