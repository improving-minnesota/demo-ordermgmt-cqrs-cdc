import { NextResponse } from 'next/server';
import { apiConfig, mockData, isDevelopment } from '../../config';

// Construct the full remote API URL from the configuration
const REMOTE_API_URL = `${apiConfig.remoteApiBaseUrl}${apiConfig.endpoints.shippingLocation}`;

export async function POST(request: Request) {
  try {
    // Parse the incoming request
    const body = await request.json();
    const { customerName, orderId } = body;
    
    // Validate customerName
    if (!customerName || typeof customerName !== 'string' || !customerName.trim()) {
      return NextResponse.json(
        { success: false, message: 'User name is required' },
        { status: 400 }
      );
    }
    
    // Prepare the data to send to the remote API
    const requestData = {
      orderId: orderId || '',
      customerName: customerName,
      customerAddress: "2115 Summit Ave.",
      zipCode: "55105",
      latitude: 44.9374829167,
      longitude: -93.1893875758
    };
    
    // Log the request details
    console.log(`Location API request (${isDevelopment ? 'DEV' : 'PROD'} mode)`);
    console.log('Request data:', JSON.stringify(requestData, null, 2));
    
    // Use mock data in development mode if configured
    if (apiConfig.useMockData) {
      console.log('Using mock data for location request (development mode)');
      
      // Simulate network delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      
      // Update mock data with actual user name and order ID if available
      const responseData = {
        ...mockData.location,
        data: {
          ...mockData.location.data,
          customerName: customerName,
          orderId: orderId || mockData.location.data.orderId
        }
      };
      
      // Return mock response
      return NextResponse.json(responseData);
    }
    
    // In production mode or when mock is disabled, make the actual API call
    console.log('Proxying location request to:', REMOTE_API_URL);
    
    // Forward the request to the remote API with timeout
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), apiConfig.timeoutMs);
    
    try {
      const response = await fetch(REMOTE_API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
        signal: controller.signal,
      });
      
      clearTimeout(timeoutId);
      
      // Parse the remote API response
      const responseData = await response.json();
      
      // Check if the response was successful
      if (!response.ok) {
        throw new Error(responseData.message || 'Remote API request failed');
      }
      
      // Return the remote API response to the client
      return NextResponse.json({
        success: true,
        message: `Location data for ${customerName} processed successfully`,
        data: responseData,
      });
    } catch (error) {
      // Handle fetch-specific errors
      const fetchError = error as { name?: string };
      if (fetchError.name === 'AbortError') {
        throw new Error(`Request to ${REMOTE_API_URL} timed out after ${apiConfig.timeoutMs}ms`);
      }
      throw error;
    }
  } catch (error) {
    console.error('Location API error:', error);
    return NextResponse.json(
      { success: false, message: 'Failed to process location data' },
      { status: 500 }
    );
  }
}
