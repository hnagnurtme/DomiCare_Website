import { cn } from '@/core/lib/utils'
import { useInView } from 'react-intersection-observer'
import React from 'react'
interface SecctionInViewProps {
  className?: string
  children: React.ReactNode
}
export default function SecctionInView({ className, children }: SecctionInViewProps) {
  const { ref, inView } = useInView({
    triggerOnce: true
  })
  return (
    <div ref={ref} className={cn(className, { 'animate-fade-up': inView })}>
      {children}
    </div>
  )
}
