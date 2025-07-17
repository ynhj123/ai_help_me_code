import { useState, useEffect } from 'react';

export interface ResponsiveState {
  isMobile: boolean;
  isTablet: boolean;
  isDesktop: boolean;
  isLargeScreen: boolean;
  screenSize: 'mobile' | 'tablet' | 'desktop' | 'large';
  width: number;
  height: number;
}

const useResponsive = (): ResponsiveState => {
  const [state, setState] = useState<ResponsiveState>({
    isMobile: false,
    isTablet: false,
    isDesktop: false,
    isLargeScreen: false,
    screenSize: 'desktop',
    width: window.innerWidth,
    height: window.innerHeight,
  });

  useEffect(() => {
    const handleResize = () => {
      const width = window.innerWidth;
      const height = window.innerHeight;
      
      const isMobile = width <= 768;
      const isTablet = width > 768 && width <= 1024;
      const isDesktop = width > 1024 && width <= 1200;
      const isLargeScreen = width > 1200;

      let screenSize: ResponsiveState['screenSize'];
      if (isMobile) {
        screenSize = 'mobile';
      } else if (isTablet) {
        screenSize = 'tablet';
      } else if (isDesktop) {
        screenSize = 'desktop';
      } else {
        screenSize = 'large';
      }

      setState({
        isMobile,
        isTablet,
        isDesktop,
        isLargeScreen,
        screenSize,
        width,
        height,
      });
    };

    // 初始检查
    handleResize();

    // 监听窗口变化
    window.addEventListener('resize', handleResize);
    
    // 监听方向变化（移动端）
    window.addEventListener('orientationchange', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
      window.removeEventListener('orientationchange', handleResize);
    };
  }, []);

  return state;
};

// 响应式断点常量
export const BREAKPOINTS = {
  MOBILE: 768,
  TABLET: 1024,
  DESKTOP: 1200,
  LARGE: 1400,
};

// 响应式工具函数
export const responsiveUtils = {
  isMobile: () => window.innerWidth <= BREAKPOINTS.MOBILE,
  isTablet: () => window.innerWidth > BREAKPOINTS.MOBILE && window.innerWidth <= BREAKPOINTS.TABLET,
  isDesktop: () => window.innerWidth > BREAKPOINTS.TABLET,
  isLargeScreen: () => window.innerWidth > BREAKPOINTS.DESKTOP,
};

export default useResponsive;