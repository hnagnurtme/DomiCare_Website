import { Header } from '@/components'
import Footer from '@/components/Footer'
import React, { Fragment } from 'react'

interface CustomerLayoutProps {
  children?: React.ReactNode
}
export default function CustomerLayout({ children }: CustomerLayoutProps) {
  return (
    <Fragment>
      <Header />
      {children}
      <Footer />
    </Fragment>
  )
}
