package com.jwt.tiendaApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jwt.tiendaApp.dtos.HttpGlobalResponseDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderRequestDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderResponseDTO;
import com.jwt.tiendaApp.dtos.OrderDTO.OrderStatusRequestDTO;
import com.jwt.tiendaApp.entity.Orders;
import com.jwt.tiendaApp.entity.Products;
import com.jwt.tiendaApp.enums.OrderStatusEnum;
import com.jwt.tiendaApp.repository.OrdersRepository;
import com.jwt.tiendaApp.repository.ProductsRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;

    public HttpGlobalResponseDTO<OrderResponseDTO> createOrder(OrderRequestDTO body, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long clientId = (Long) request.getAttribute("userId");

        Optional<Products> productOpt = productsRepository.findById(body.getProductId());

        if (productOpt.isEmpty()) {
            response.setMessage("Producto no encontrado (404)");
            response.setData(null);
            return response;
        }

        Products product = productOpt.get();

        if (product.getStock() < body.getQuantity()) {
            response.setMessage("¡Pilaseeees!, no hay stock suficiente (400)");
            response.setData(null);
            return response;
        }

        product.setStock(product.getStock() - body.getQuantity());
        productsRepository.save(product);

        Double total = product.getPrice() * body.getQuantity();

        Orders order = new Orders();
        order.setClientId(clientId);
        order.setProductId(body.getProductId());
        order.setQuantity(body.getQuantity());
        order.setTotal(total);
        order.setStatus(OrderStatusEnum.PENDIENTE.name());

        Orders saved = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(saved.getId());
        dto.setClientId(saved.getClientId());
        dto.setProductId(saved.getProductId());
        dto.setQuantity(saved.getQuantity());
        dto.setTotal(saved.getTotal());
        dto.setStatus(saved.getStatus());
        dto.setCreatedAt(saved.getCreatedAt());

        response.setMessage("¡Pedido creado con éxito mi papacho! 📦");
        response.setData(dto);
        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getMyOrders(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long clientId = (Long) request.getAttribute("userId");

        List<Orders> clientOrders = ordersRepository.findByClientId(clientId);
        List<OrderResponseDTO> orderDTOList = new ArrayList<>();

        for (Orders o : clientOrders) {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(o.getId());
            dto.setClientId(o.getClientId());
            dto.setProductId(o.getProductId());
            dto.setQuantity(o.getQuantity());
            dto.setTotal(o.getTotal());
            dto.setStatus(o.getStatus());
            dto.setCreatedAt(o.getCreatedAt());
            orderDTOList.add(dto);
        }

        response.setMessage("Tus pedidos realizados:");
        response.setData(orderDTOList);
        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getMySales(HttpServletRequest request) {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();

        Long sellerId = (Long) request.getAttribute("userId");

        List<Orders> allOrders = ordersRepository.findAll();
        List<OrderResponseDTO> sellerSales = new ArrayList<>();

        for (Orders o : allOrders) {
            Optional<Products> productOpt = productsRepository.findById(o.getProductId());
            if (productOpt.isPresent() && productOpt.get().getSellerId().equals(sellerId)) {
                OrderResponseDTO dto = new OrderResponseDTO();
                dto.setId(o.getId());
                dto.setClientId(o.getClientId());
                dto.setProductId(o.getProductId());
                dto.setQuantity(o.getQuantity());
                dto.setTotal(o.getTotal());
                dto.setStatus(o.getStatus());
                dto.setCreatedAt(o.getCreatedAt());
                sellerSales.add(dto);
            }
        }

        response.setMessage("Ventas de tus productos:");
        response.setData(sellerSales);
        return response;
    }

    public HttpGlobalResponseDTO<List<OrderResponseDTO>> getAllOrders() {
        HttpGlobalResponseDTO<List<OrderResponseDTO>> response = new HttpGlobalResponseDTO<>();
        List<Orders> allOrders = ordersRepository.findAll();
        List<OrderResponseDTO> orderDTOList = new ArrayList<>();

        for (Orders o : allOrders) {
            OrderResponseDTO dto = new OrderResponseDTO();
            dto.setId(o.getId());
            dto.setClientId(o.getClientId());
            dto.setProductId(o.getProductId());
            dto.setQuantity(o.getQuantity());
            dto.setTotal(o.getTotal());
            dto.setStatus(o.getStatus());
            dto.setCreatedAt(o.getCreatedAt());

            orderDTOList.add(dto);
        }

        response.setMessage("Todos los pedidos del sistema (ADMIN):");
        response.setData(orderDTOList);
        return response;
    }

    public HttpGlobalResponseDTO<OrderResponseDTO> updateStatus(Long orderId, OrderStatusRequestDTO status,
            HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long currentUserId = (Long) request.getAttribute("userId");
        Long currentUserRolId = (Long) request.getAttribute("rolId");

        try {
            OrderStatusEnum.valueOf(status.getStatus().toUpperCase());
        } catch (Exception e) {
            response.setMessage(
                    "¡Ey parcero!, ese estado no existe, los válidos son: PENDIENTE, ENVIADO, ENTREGADO, CANCELADO 🚫");
            response.setData(null);
            return response;
        }

        Optional<Orders> orderOpt = ordersRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            response.setMessage("¡Pedido no encontrado mi llave! (404)");
            response.setData(null);
            return response;
        }

        Orders order = orderOpt.get();

        if (currentUserRolId == 3) {
            Optional<Products> productOpt = productsRepository.findById(order.getProductId());
            if (productOpt.isPresent() && !productOpt.get().getSellerId().equals(currentUserId)) {
                response.setMessage("¡Alto ahí!, no puedes cambiar el estado de un pedido que no es tuyo (403)");
                response.setData(null);
                return response;
            }
        }

        order.setStatus(status.getStatus().toUpperCase());
        Orders updated = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(updated.getId());
        dto.setClientId(updated.getClientId());
        dto.setProductId(updated.getProductId());
        dto.setQuantity(updated.getQuantity());
        dto.setTotal(updated.getTotal());
        dto.setStatus(updated.getStatus());
        dto.setCreatedAt(updated.getCreatedAt());

        response.setMessage("¡Estado del pedido actualizado con toda! ✅");
        response.setData(dto);
        return response;
    }

    public HttpGlobalResponseDTO<OrderResponseDTO> cancelOrder(Long orderId, HttpServletRequest request) {
        HttpGlobalResponseDTO<OrderResponseDTO> response = new HttpGlobalResponseDTO<>();

        Long clientId = (Long) request.getAttribute("userId");

        Optional<Orders> orderOpt = ordersRepository.findById(orderId);

        if (orderOpt.isEmpty()) {
            response.setMessage("¡Pedido no encontrado mi llave! (404)");
            response.setData(null);
            return response;
        }

        Orders order = orderOpt.get();

        if (!order.getClientId().equals(clientId)) {
            response.setMessage("¡Ey!, ese pedido no te pertenece (403)");
            response.setData(null);
            return response;
        }

        if (!order.getStatus().equalsIgnoreCase(OrderStatusEnum.PENDIENTE.name())) {
            response.setMessage("¡Pilas!, solo puedes cancelar pedidos en estado PENDIENTE (400)");
            response.setData(null);
            return response;
        }

        Optional<Products> productOpt = productsRepository.findById(order.getProductId());
        if (productOpt.isPresent()) {
            Products product = productOpt.get();
            product.setStock(product.getStock() + order.getQuantity());
            productsRepository.save(product);
        }

        order.setStatus(OrderStatusEnum.CANCELADO.name());
        Orders cancelled = ordersRepository.save(order);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(cancelled.getId());
        dto.setClientId(cancelled.getClientId());
        dto.setProductId(cancelled.getProductId());
        dto.setQuantity(cancelled.getQuantity());
        dto.setTotal(cancelled.getTotal());
        dto.setStatus(cancelled.getStatus());
        dto.setCreatedAt(cancelled.getCreatedAt());

        response.setMessage("¡Pedido cancelado y stock devuelto con éxito! 🔄");
        response.setData(dto);
        return response;
    }

}
